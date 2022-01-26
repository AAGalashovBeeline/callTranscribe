package apiservice.calls

import io.circe.{Codec}
import java.time.Instant

object Model {

  sealed trait Request
  sealed trait Response

  case class CreateCallReq(externalCallId: String, ani: String) extends  Request
  case class CreateCallResp(id: Long) extends Response

  case class CreateTranscribeReq(callId: Long, transcribe: String) extends  Request
  case class CreateTranscribeResp(id: Long, callId: Long, transcribe: String) extends Response

  case class CallInfo(id: Long, externalCallId: String, ani: String, date: Instant) extends  Response
  case class CallsInfoResp(calls: List[CallInfo]) extends  Response

  case class CallFilter(ani: String, dateFrom: Instant, dateTo: Option[Instant]) extends  Request
  case class CallListResponse(calls: List[CallInfo], page: Int, limit: Int, count: Long, filter: Option[CallFilter]) extends Response

  implicit val CreateCallReqCodec: Codec[CreateCallReq] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateCallRespCodec: Codec[CreateCallResp] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateTranscribeReqCodec: Codec[CreateTranscribeReq] = io.circe.generic.semiauto.deriveCodec
  implicit val CreateTranscribeRespCodec: Codec[CreateTranscribeResp] = io.circe.generic.semiauto.deriveCodec
  implicit val CallInfoCodec: Codec[CallInfo] = io.circe.generic.semiauto.deriveCodec
  implicit val CallFilterCodec: Codec[CallFilter] = io.circe.generic.semiauto.deriveCodec
}
