package apiservice.calls.LogicsTests

import apiservice.calls.Mock.MockConf
import apiservice.calls.config.Configuration
import apiservice.calls.repository.Logics.HasLogicsClient
import apiservice.calls.repository.{DbTransactor, Logics}
import zio.clock.Clock
import zio.{Task, ULayer, ZIO}
import zio.test._
import zio.test.Assertion._

object LogicsTest {

  //val dbTransactor: ULayer[DbTransactor] = Configuration.live >>> DbTransactor.postgres
  val dbTransactor: ULayer[DbTransactor] = MockConf.testLive >>> DbTransactor.postgres
  val env: ULayer[HasLogicsClient] = dbTransactor >>> Logics.live

  def createCallPBX(externalCallId: String, ani: String) =
    Logics.Service.createCallPBX("sdfd", "asds").run.provideLayer(env)


  val test: Spec[Any, TestFailure[Throwable], TestSuccess] = suite("Тесты сервиса Logics")(
    suite("Тесты на проверку Logics")(
      testM("Тест на создание звонка") {

        assertM(createCallPBX("sdfd", "asds"))(succeeds(anything))
      },
      testM("Тест 1 + 1") {
        val userReg = ZIO.succeed(1 + 1)

        assertM(userReg)(equalTo(2))
      },
    )
  )
}
