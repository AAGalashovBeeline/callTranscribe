package apiservice.calls

import apiservice.calls.Model._
import apiservice.calls.repository.Logics
import apiservice.calls.repository.Logics.{HasLogicsClient}
import io.circe.generic.auto._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir._
import sttp.tapir.generic.auto._
import zio.{Has, URIO, ZIO}
import zio.interop.catz._


object CallService {

  val tapEP: Seq[ZServerEndpoint[HasLogicsClient, _ <: Request, String, _ <: Response]] = List(
       sttp.tapir.endpoint
        .post
        .in("pbx"/"create-call")
        .in(jsonBody[CreateCallReq])
        .out(jsonBody[CreateCallResp])
         .errorOut {
           oneOf[String](
             oneOfDefaultMapping(jsonBody[String].description("unknown"))
           )
         }
        .zServerLogic({ req =>
            Logics.Service.createCallPBX(req.externalCallId, req.ani)
              .map(CreateCallResp)
              .mapError(e => e.toString)
        }),

    sttp.tapir.endpoint
      .post.description("транскриб из АТС")
      .in("pbx"/"create-transcribe")
      .in(jsonBody[CreateTranscribeReq])
      .out(jsonBody[CreateTranscribeResp])
      .errorOut {
        oneOf[String](
          oneOfDefaultMapping(jsonBody[String].description("unknown"))
        )
      }
      .zServerLogic({ req =>
        Logics.Service.createTranscribe(req.callId, req.transcribe)
          .mapError(e => e.toString)
      }),

    sttp.tapir.endpoint
      .post
      .in("pbx"/"get-calls")
      .in(jsonBody[CallFilter])
      .out(jsonBody[CallsInfoResp])
      .errorOut {
        oneOf[String](
          oneOfDefaultMapping(jsonBody[String].description("unknown"))
        )
      }
      .zServerLogic { req =>
        Logics.Service.getCalls(req.ani, req.dateFrom, req.dateTo)
          .map(CallsInfoResp)
          .mapError(e => e.toString)
      }

  )
}
