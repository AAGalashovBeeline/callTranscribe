package apiservice.calls.Mock

import apiservice.calls.config.Configuration.{DbConfig}
import zio.{Has, ZIO, ZLayer}

object MockConf {
  val testLive: ZLayer[Any, Nothing, Has[DbConfig]] = ZIO.succeed(
    new DbConfig(
    "asda",
      "asd",
      "asdasd",
      "asdsad",
  )).toLayer
}
