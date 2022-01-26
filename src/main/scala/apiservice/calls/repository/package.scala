package apiservice.calls

import apiservice.calls.config.Configuration.DbConfig
import doobie.util.transactor.Transactor
import zio.{Has, Task, URLayer, ZLayer}
import zio.interop.catz._


package object repository {

  type DbTransactor = Has[DbTransactor.Resource]

  implicit val zioRuntime: zio.Runtime[zio.ZEnv] = zio.Runtime.default

  object DbTransactor {
    trait Resource {
      val dbConnect: Transactor[Task]
    }

    val postgres: URLayer[Has[DbConfig], DbTransactor] = ZLayer.fromService { db =>
      new Resource {
        val dbConnect: Transactor[Task] =
          Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password)
      }
    }
  }
}
