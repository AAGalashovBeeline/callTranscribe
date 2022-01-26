//package apiservice.calls.config
//
//import apiservice.calls.config.GlobalCfg.HasConfig
//import zio.{URIO, ZIO}
//
//case class DbConfig(driver: String, url: String, user: String, password: String)
//
//case class AppConfig(db: DbConfig)
//
//object AppConfig {
//  def get: URIO[HasConfig, AppConfig] = ZIO.environment[HasConfig].flatMap(_.get.getConfig)
//}
//
