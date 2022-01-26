package apiservice.calls

import apiservice.calls.config.Configuration
import apiservice.calls.repository.Logics.HasLogicsClient
import apiservice.calls.repository.{DbTransactor111, DbTransactor222, Logics}
import zio.ULayer
import zio.blocking.Blocking
import zio.clock.Clock


object Environments {
  //без Clock билдится в 1й раз с ошибкой имплисита
  type HttpServerEnvironment = Configuration with Clock //??? зачем Clock???
  type AppEnvironment = HttpServerEnvironment with HasLogicsClient

  val httpServerEnvironment: ULayer[HttpServerEnvironment] = Configuration.live ++ Clock.live
  //">>>" подает выходные сервисы этого уровня на вход указанного уровня
  //чтобы заполнить конфигом тразактор, нужны данные из конфига (Has[DbConfig]) ++
  //++ мапим в Has[DbConfig] и Has[HttpServerConfig], который объединяется в type Configuration (в пакете config)
  val dbTransactor: ULayer[DbTransactor111] = Configuration.live >>> DbTransactor222.postgres
  //репозиторий принимает слой? транзактора
  val logicsClient: ULayer[HasLogicsClient] = dbTransactor >>> Logics.live
  //итоговый слой конфигурации (Has[DbConfig] и Has[HttpServerConfig]) и методов работы с бд (с тразактором)
  val appEnvironment = httpServerEnvironment ++ logicsClient ++ Blocking.live


}
