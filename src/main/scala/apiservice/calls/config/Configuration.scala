package apiservice.calls.config

import pureconfig.ConfigSource
import pureconfig.generic.auto._
import zio.{Has, ULayer, ZIO}

object Configuration {

  final case class DbConfig(driver: String, url: String, user: String, password: String)
  final case class AppConfig(database: DbConfig)

  val live: ULayer[Configuration] =
    ZIO
    .effect(ConfigSource.default.loadOrThrow[AppConfig])
    .map(c => Has(c.database))
    .orDie.toLayerMany
}
