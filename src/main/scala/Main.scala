import apiservice.calls.CallService
import apiservice.calls.Environments.{appEnvironment}
import apiservice.calls.repository.Logics.HasLogicsClient
import zio.blocking.Blocking
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.openapi.{Contact, Info}
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.ziohttp.SwaggerZioHttp
import zhttp.http._
import zhttp.service.Server
import zio._


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

  val ep: Http[HasLogicsClient, Throwable, Request, Response[HasLogicsClient, Throwable]] =
    CallService.tapEP.map(ZioHttpInterpreter().toHttp(_)).reduce(_ <> _)

  def run(args: List[String]): ZIO[ZEnv, Nothing, ExitCode] = {
    val program = for {
      _ <- Server.start(3000,ep <> swagger)
    } yield ()

    program.provideLayer(appEnvironment).exitCode
  }
}
