package apiservice.calls

import apiservice.calls.config.Configuration.{DbConfig}
import zio.Has

package object config {

  type Configuration = Has[DbConfig]
}
