package apiservice.calls

import apiservice.calls.Logics._
import apiservice.calls.Model._
import io.circe.generic.auto._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir._

import sttp.tapir.generic.auto._

object CallService {

  val tapEP: Seq[ZServerEndpoint[Any, _ <: Request, String, _ <: Response]] = List(
    //   sttp.tapir.endpoint
    //    .post
    //    .in("createCall")
    //    .in(jsonBody[CreateCallReq])
    //    .out(jsonBody[Long])
    //    .zServerLogic({ req =>
    //      ZIO.succeed(
    //        unsafeRunTask(createCallPBX(req.externalCallId, req.ani))
    //      )
    //    }),
    //
    sttp.tapir.endpoint
      .post
      .in("createTranscribe")
      .in(jsonBody[CreateTranscribeReq])
      .out(jsonBody[CreateTranscribeResp])
      .errorOut {
        oneOf[String](
          oneOfDefaultMapping(jsonBody[String].description("unknown"))
        )
      }
      .zServerLogic({ req =>
        createTranscribe(req.callId, req.transcribe)
          .mapError(e => e.toString)
      })
    ,

    sttp.tapir.endpoint
      .post
      .in("getCalls")
      .in(jsonBody[CallFilter])
      .out(jsonBody[CallsInfoResp])
      .errorOut {
        oneOf[String](
          oneOfDefaultMapping(jsonBody[String].description("unknown"))
        )
      }
      .zServerLogic { req =>
        getCalls(req.ani, req.dateFrom, req.dateTo)
          .map(CallsInfoResp)
          .mapError(e => e.toString)
      }

  )
}
