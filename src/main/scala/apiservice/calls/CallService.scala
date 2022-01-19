package apiservice.calls

import apiservice.calls.Logics._
import apiservice.calls.Model._
import io.circe.generic.auto._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir._
import sttp.tapir.generic.auto._
import zio.ZIO

object CallService {

  val tapEP: Seq[ZServerEndpoint[Any, _ <: Request, String, _ <: Response]] = List(
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
            createCallPBX(req.externalCallId, req.ani)
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
      //18-01 getCalls возвращаем List[CallInfo]
      .zServerLogic { req =>
        getCalls(req.ani, req.dateFrom, req.dateTo)
          .map(CallsInfoResp)   //CallInfoResp хранит в себе List[CallInfo]
          .mapError(e => e.toString)
      }

  )
}
