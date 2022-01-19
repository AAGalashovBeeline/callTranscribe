package apiservice.calls

import zio._

//object TestLayer extends zio.App {
object TestLayer {

  // Define our simple ZIO program
  val zio: ZIO[Has[String], Nothing, Unit] = for {
    name <- ZIO.access[Has[String]](_.get)
    _    <- UIO(println(s"Hello, $name!"))
  } yield ()

  // Create a ZLayer that produces a string and can be used to satisfy a string
  // dependency that the program has
 // val nameLayer: ULayer[Has[String]] = ZLayer.succeed("Adam")

  // Run the program, providing the `nameLayer`
//  def run(args: List[String]): URIO[ZEnv, ExitCode] =
//    zio.provideLayer(nameLayer).as(ExitCode.success)
}