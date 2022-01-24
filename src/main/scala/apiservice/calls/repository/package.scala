package apiservice.calls

import apiservice.calls.config.Configuration.DbConfig
import doobie.util.transactor.Transactor
import zio.{Has, Task, URLayer, ZLayer}
import zio.interop.catz._             //?????


package object repository {

  //Has[DbTransactor222.Resource] объект с трейтом Resource и одним полем val xa: Transactor[Task]
  type DbTransactor111 = Has[DbTransactor222.Resource]

  implicit val zioRuntime: zio.Runtime[zio.ZEnv] = zio.Runtime.default

  object DbTransactor222 {
    trait Resource {
      val dbConnect: Transactor[Task]
    }

    //URLayer[Has[DbConfig], DbTransactor] указываем, какие слои предоставляем
    //предоставляем type DbTransactor111 (Has[DbTransactor222.Resource] ++
    //++ объект с трейтом Resource и одним полем val xa: Transactor[Task])
    val postgres: URLayer[Has[DbConfig], DbTransactor111] = ZLayer.fromService { db =>
      new Resource {
        val dbConnect: Transactor[Task] =
          Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password)
      }
    }
  }
}
