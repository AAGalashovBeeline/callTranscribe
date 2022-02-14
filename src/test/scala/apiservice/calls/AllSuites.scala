package apiservice.calls

import apiservice.calls.LogicsTests.LogicsTest
import zio._
import zio.console._
import zio.test._
import zio.test.environment._

object AllSuites extends DefaultRunnableSpec {
  def sayHello: ZIO[Console, Any, Unit] =
    console.putStrLn("Hello, World!")

  override def spec: Spec[TestEnvironment, TestFailure[Throwable], TestSuccess] =
    suite("All tests")(
      LogicsTest.test
    )

}