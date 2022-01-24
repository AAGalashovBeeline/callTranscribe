package apiservice.calls

import apiservice.calls.config.Configuration.{DbConfig, HttpServerConfig}
import zio.Has

package object config {

  type Configuration = Has[DbConfig] with Has[HttpServerConfig]
}
