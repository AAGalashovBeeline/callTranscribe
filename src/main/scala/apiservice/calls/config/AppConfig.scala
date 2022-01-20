//package apiservice.calls.config
//
//import zio.{URIO, ZIO}
//
//case class TelegramCfg(token: String, url: String, timeout: Int)
//case class AggApiCfg(url: String, version: String)
//case class DBCfg(uri: String)
//
//
//case class AppConfig(telegram: TelegramCfg, aggapi: AggApiCfg, db: DBCfg)
//
//object AppConfig {
//  def get: URIO[HasConfig, AppConfig] = ZIO.environment[HasConfig].flatMap(_.get.getConfig)
//}
//
