package apiservice.calls.util


import zio.{Has, Tag, ZLayer}

class LayerOps[In, E, Out](
                            private val self: ZLayer[In, E, Has[Out]]
                          ) extends AnyVal {

  def \[NewOut](f: Out => NewOut)(
    implicit
    ev1: Tag[Out],
    ev2: Tag[NewOut]
  ): ZLayer[In, E, Has[NewOut]] =
    self map { it => Has(f(it.get)) }
}

object LayerOps {
  implicit def toLayerOps[In, E, Out](self: ZLayer[In, E, Has[Out]]): LayerOps[In, E, Out] = new LayerOps(self)
}

