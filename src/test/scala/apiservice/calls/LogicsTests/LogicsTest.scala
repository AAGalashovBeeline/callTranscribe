package apiservice.calls.LogicsTests

import apiservice.calls.config.Configuration
import apiservice.calls.repository.Logics.HasLogicsClient
import apiservice.calls.repository.{DbTransactor, Logics}
import zio.clock.Clock
import zio.{Task, ULayer, ZIO}
import zio.test._
import zio.test.Assertion._

object LogicsTest {
//  val userProfile = new UserProfile("vasiliyshukshin@gmail.com",
//    "ab31b302-13bb-5e6f-4c89748d28e1",
//    "beeline",
//    List(SuperAdminRole),
//    GroupsCfg(),
//    "")
//
  type HttpServerEnvironment = Configuration with Clock
  type AppEnvironment = HttpServerEnvironment with HasLogicsClient

  val httpServerEnvironment: ULayer[HttpServerEnvironment] = Configuration.live ++ Clock.live
  val dbTransactor: ULayer[DbTransactor] = Configuration.live >>> DbTransactor.postgres
  val logicsClient: ULayer[HasLogicsClient] = dbTransactor >>> Logics.live

  val env = logicsClient

//Logger.live ++ MockKeycloak.testLive ++ MockUser.testLive(
//    Some(new User(Some(userProfile),
//      Some("tenant")
//    )))

  def createCallPBX(externalCallId: String, ani: String) =
    Logics.Service.createCallPBX("sdfd", "asds").run.provideLayer(env)


//  def generateUser(email: String): UserRegistration = UserRegistration(email, "Шукшин", "Василий",
//    Option("Макарович"), true, true, true, true, true
//  )
//
//  def createAccount(reg: UserRegistration) =
//    Logics.registerUser(reg).run.provideLayer(env)

  val test: Spec[Any, TestFailure[Throwable], TestSuccess] = suite("Тесты сервиса Logics")(
    suite("Тесты на проверку Logics")(
      testM("Тест на создание звонка") {
        val userReg = ZIO.succeed(1 + 1)

        assertM(createCallPBX("sdfd", "asds"))(succeeds(anything))
      },
      testM("Тест 1 + 1") {
        val userReg = ZIO.succeed(1 + 1)

        assertM(userReg)(equalTo(2))
      },
    )
  )
}
