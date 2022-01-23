import apiservice.calls.CallService
import apiservice.calls.repository.CallsRepository.HasLogicsClient
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.openapi.{Contact, Info}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.ziohttp.SwaggerZioHttp
import zhttp.http._
import zhttp.service.Server
import zio._
import zio.blocking.Blocking


object Main extends zio.App {

  val globalInfo: Info = Info(
    "VoiceAudioPlatform",
    "1.0",
    Some("API backend"),
    None,
    Some(Contact(Some("Novgorodov Aleksey"), Some("aanovgorodov@beeline.ru"), None)),
    None
  )

  private val swagger: Http[Blocking, Throwable, Request, Response[Blocking, Throwable]] = {
    val ep = CallService.tapEP.map(e => e.endpoint)
    new SwaggerZioHttp(OpenAPIDocsInterpreter().toOpenAPI(ep, globalInfo).toYaml).route
  }

  val business: ZIO[Any, Throwable, Nothing] = ZIO.effect(println("1")) *> ZIO.fail(new Throwable)

  val ep1: HttpApp[Any, Throwable] = HttpApp.collectM {
    case Method.GET -> Root/"ep1" => ZIO.succeed(Response.text("ok!"))

    case req @ Method.POST -> Root/"ep2" =>
      println(req.getBodyAsString.getOrElse("kek"))
      ZIO.succeed(Response.text(req.getBodyAsString.getOrElse("kek")))
  }

  val ep: Http[HasLogicsClient, Throwable, Request, Response[HasLogicsClient, Throwable]] = CallService.tapEP.map(ZioHttpInterpreter().toHttp(_)).reduce(_ <> _)
  val webServer: ZIO[Blocking with HasLogicsClient, Throwable, Nothing] = Server.start(3000,ep <> swagger)


  //19-01 tutorial
  val zio: ZIO[Has[String], Nothing, Unit] = for {
    name <- ZIO.access[Has[String]](_.get)
    _    <- UIO(println(s"Hello, $name!"))
  } yield ()

  //tutorial
  val nameLayer: ULayer[Has[String]] = ZLayer.succeed("Adam")

  def run(args: List[String]): URIO[ZEnv, ExitCode] =
    zio.provideLayer(nameLayer).exitCode

  webServer
    .fold(e => {
      println(e)
      ExitCode.apply(-666)
    }, _ => ExitCode.success)
}
