package apiservice.calls.LogicsTests

import apiservice.calls.Mock.{MockConf, MockDbTransactor, MockLogics}
import apiservice.calls.config.Configuration
import apiservice.calls.repository.Logics.HasLogicsClient
import apiservice.calls.repository.{DbTransactor, Logics}
import zio.clock.Clock
import zio.{Task, ULayer, ZIO}
import zio.test._
import zio.test.Assertion._

object LogicsTest {

  val dbTransactor: ULayer[DbTransactor] = MockConf.testLive >>> MockDbTransactor.postgres
  val env: ULayer[HasLogicsClient] = dbTransactor >>> MockLogics.testLive

  def createCallPBX(externalCallId: String, ani: String) =
    Logics.createCallPBX("sdfd", "asds").run.provideLayer(env)


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
