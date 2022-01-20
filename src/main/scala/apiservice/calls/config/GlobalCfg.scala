package apiservice.calls.config

import zio.{Has, UIO, ULayer, ZIO}

object GlobalCfg {
  type HasConfig = Has[GlobalCfg.Service]

  trait Service {
    def getConfig: UIO[AppConfig]
  }

  def fromConfig(appConfig: AppConfig): Service = new Service {
    override def getConfig: UIO[AppConfig] = ZIO.succeed(appConfig)
  }

  val live: ULayer[HasConfig] = {
    def impureConfig: AppConfig = {
      import pureconfig._
      import pureconfig.generic.auto._                //для генерации конфиг файлов
      ConfigSource.default.loadOrThrow[AppConfig]
    }

    ZIO.effect(impureConfig).map(fromConfig).orDie
  }.toLayer

}
