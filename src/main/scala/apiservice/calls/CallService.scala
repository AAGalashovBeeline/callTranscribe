package apiservice.calls

import apiservice.calls.Model._
import apiservice.calls.repository.Logics
import apiservice.calls.repository.Logics.{HasLogicsClient, createCallPBX111}
import io.circe.generic.auto._
import sttp.tapir.generic.auto.schemaForCaseClass
import sttp.tapir.json.circe.jsonBody
import sttp.tapir.ztapir._
import sttp.tapir.generic.auto._
import zio.{Has, URIO, ZIO}
import zio.interop.catz._

//extends Logics.Service
object CallService {

//  val asdsad: URIO[Has[CallsRepository.Service], CallsRepository.Service] = ZIO.service[CallsRepository.Service]
//  type Logics = Has[CallsRepository.Service]
//
//  val asdsa = for {
//    tg <- ZIO.service[CallsRepository.Service]
//  } Yield tg


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
            createCallPBX111(req.externalCallId, req.ani)
              .map(CreateCallResp)
              .mapError(e => e.toString)
        })  //,

//    sttp.tapir.endpoint
//      .post.description("транскриб из АТС")
//      .in("pbx"/"create-transcribe")
//      .in(jsonBody[CreateTranscribeReq])
//      .out(jsonBody[CreateTranscribeResp])
//      .errorOut {
//        oneOf[String](
//          oneOfDefaultMapping(jsonBody[String].description("unknown"))
//        )
//      }
//      .zServerLogic({ req =>
//        createTranscribe(req.callId, req.transcribe)
//          .mapError(e => e.toString)
//      })
//    ,
//
//    sttp.tapir.endpoint
//      .post
//      .in("pbx"/"get-calls")
//      .in(jsonBody[CallFilter])
//      .out(jsonBody[CallsInfoResp])
//      .errorOut {
//        oneOf[String](
//          oneOfDefaultMapping(jsonBody[String].description("unknown"))
//        )
//      }
//      .zServerLogic { req =>
//        getCalls(req.ani, req.dateFrom, req.dateTo)
//          .map(CallsInfoResp)
//          .mapError(e => e.toString)
//      }

  )
}
