package apiservice.calls.config

import pureconfig.ConfigSource
import pureconfig.generic.auto._
import zio.{Has, ULayer, ZIO, ZLayer}

object Configuration {

  final case class DbConfig(driver: String, url: String, user: String, password: String)
  //final case class HttpServerConfig(host: String, port: Int, path: String)
  final case class AppConfig(database: DbConfig)

  //ZLayer.fromEffectMany???
  val live: ULayer[Configuration] =
    ZIO
      //default по-умолчанию из application.conf дергает инфу
    .effect(ConfigSource.default.loadOrThrow[AppConfig])
      //мапим в Has[DbConfig] и Has[HttpServerConfig], который объединяется в type Configuration (в пакете config)
    .map(c => Has(c.database))
    .orDie.toLayerMany
}
