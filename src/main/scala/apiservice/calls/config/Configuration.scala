package apiservice.calls.config

import pureconfig.generic.auto._        //для загрузки файлов конфигурации

object Configuration {
  //final case class AppConfig(database: DbConfig, httpServer: HttpServerConfig)
  final case class DbConfig(driver: String, url: String, user: String, password: String)
  //final case class HttpServerConfig(host: String, port: Int, path: String)
}
