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

    //ZLayer.fromService[DbConfig, DbTransactor111] так должно быть написано? из левого получаем правый
    //"db =>" это DbConfig
    val postgres: URLayer[Has[DbConfig], DbTransactor111] = ZLayer.fromService { db =>
      //создание анонимного экземпляра анонимного класса
      new Resource {
        //реализовать нужно только 1 метод. Даже если внутри Resource их 100
        val dbConnect: Transactor[Task] =
          Transactor.fromDriverManager[Task](db.driver, db.url, db.user, db.password)
      }
    }
  }
}
