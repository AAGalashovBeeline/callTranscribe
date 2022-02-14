package apiservice.calls.Mock

import apiservice.calls.Model
import apiservice.calls.repository.Logics
import apiservice.calls.repository.Logics.Service
import zio.{Has, Task, ZIO, ZLayer}

import java.time.Instant

object MockLogics {
  val testLive: ZLayer[Any, Nothing, Has[Logics.Service]] = ZIO.succeed {
    new Service {
      override def createCallPBX(externalCallId: String, ani: String): Task[Long] = ???

      override def createTranscribe(callId: Long, transcribe: String): Task[Model.CreateTranscribeResp] = ???

      override def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[Model.CallInfo]] = ???
    }
  }.toLayer
}
