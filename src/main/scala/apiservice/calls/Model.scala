package apiservice.calls

import io.circe.{Codec, Decoder, Encoder, HCursor, Json}
import sttp.tapir.Schema
import sttp.tapir.SchemaType.{SInteger, SString}

import java.sql.Timestamp
import java.time.Instant

object Model {

  sealed trait Request
  sealed trait Response

  case class CreateCallReq(externalCallId: String, ani: String) extends  Request
  case class CreateCallResp(id: Long) extends Response

  case class CreateTranscribeReq(callId: Long, transcribe: String) extends  Request
  case class CreateTranscribeResp(id: Long, callId: Long, transcribe: String) extends Response

  case class CallInfo(id: Long, externalCallId: String, ani: String, date: Timestamp) extends  Response
  case class CallsInfoResp(calls: List[CallInfo]) extends  Response


  case class CallFilter(ani: String, dateFrom: Instant, dateTo: Option[Instant]) extends  Request

  case class CallListResponse(calls: List[CallInfo], page: Int, limit: Int, count: Long, filter: Option[CallFilter]) extends Response



  implicit val CreateCallReqCodec: Codec[CreateCallReq] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateCallRespCodec: Codec[CreateCallResp] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateTranscribeReqCodec: Codec[CreateTranscribeReq] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateTranscribeRespCodec: Codec[CreateTranscribeResp] = io.circe.generic.semiauto.deriveCodec


  implicit val CallInfoCodec: Codec[CallInfo] = io.circe.generic.semiauto.deriveCodec
  implicit val CallFilterCodec: Codec[CallFilter] = io.circe.generic.semiauto.deriveCodec

//  implicit val InstantFormat: InstantFormatter = InstantFormat.forPattern("dd.MM.yyyy")
//  implicit val schemaForTimestamp: Schema[Timestamp] = Schema(SInteger())
//  implicit val TimestampFormat : Encoder[Timestamp] with Decoder[Timestamp] = new Encoder[Timestamp] with Decoder[Timestamp] {
//    override def apply(a: Timestamp): Json = Encoder.encodeLong.apply(a.getTime)
//    override def apply(c: HCursor): Decoder.Result[Timestamp] = Decoder.decodeLong.map(s => new Timestamp(s)).apply(c)
//  }
//
//  implicit val schemaForInstant: Schema[Instant] = Schema(SString())
//  implicit val InstantCodec : Encoder[Instant] with Decoder[Instant] = new Encoder[Instant] with Decoder[Instant] {
//    override def apply(a: Instant): Json = Encoder.encodeString.apply(a.toString(InstantFormat))
//    override def apply(c: HCursor): Decoder.Result[Instant] = Decoder.decodeString.map(s => Instant.parse(s, InstantFormat)).apply(c)
//  }



}
