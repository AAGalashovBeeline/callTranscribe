package apiservice.calls.Mock

import apiservice.calls.repository.DbTransactor
import apiservice.calls.repository.DbTransactor.Resource
import doobie.util.transactor.Transactor
import zio.{Has, Task, ZIO, ZLayer}
import zio.interop.catz._

object MockDbTransactor {

  implicit val zioRuntime: zio.Runtime[zio.ZEnv] = zio.Runtime.default

  val postgres: ZLayer[Any, Nothing, Has[DbTransactor.Resource]] = ZIO.succeed(
    new Resource {
      override val dbConnect: Transactor[Task] =
        Transactor.fromDriverManager[Task](
          "0",
          "0",
          "0",
          "0")
    }
  ).toLayer
}
