package apiservice.calls

import apiservice.calls.config.Configuration
import apiservice.calls.repository.Logics.HasLogicsClient
import apiservice.calls.repository.{DbTransactor, Logics}
import zio.ULayer
import zio.blocking.Blocking
import zio.clock.Clock


object Environments {

  type HttpServerEnvironment = Configuration with Clock
  type AppEnvironment = HttpServerEnvironment with HasLogicsClient

  val httpServerEnvironment: ULayer[HttpServerEnvironment] = Configuration.live ++ Clock.live
  val dbTransactor: ULayer[DbTransactor] = Configuration.live >>> DbTransactor.postgres
  val logicsClient: ULayer[HasLogicsClient] = dbTransactor >>> Logics.live
  val appEnvironment = httpServerEnvironment ++ logicsClient ++ Blocking.live
}
