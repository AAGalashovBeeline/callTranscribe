package apiservice.calls.Mock

import apiservice.calls.config.Configuration.{AppConfig, DbConfig}
import zio.{Has, ZIO, ZLayer}

object MockConf {
  val testLive: ZLayer[Any, Nothing, Has[AppConfig]] = ZIO.succeed(AppConfig(
    new DbConfig(
    "asda",
      "asd",
      "asdasd",
      "asdsad",
  ))).toLayer
}
