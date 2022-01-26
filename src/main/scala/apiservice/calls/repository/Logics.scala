package apiservice.calls.repository

import apiservice.calls.Model.{CallInfo, CreateTranscribeResp}
import doobie._
import doobie.implicits._
import zio._
import zio.interop.catz._
import java.time.Instant
import doobie.postgres.implicits._
import zio.macros.accessible


class Logics(dbConnect: Transactor[Task]) extends Logics.Service {

  def createCallPBX(externalCallId: String, ani: String): Task[Long] =
    sql"""
        INSERT INTO CALLS ("external_callId", ani)
        VALUES ($externalCallId, $ani) RETURNING id
       """
      .query[Long]
      .unique
      .transact(dbConnect)

  def createTranscribe(callId: Long, transcribe: String): Task[CreateTranscribeResp] =
    sql"""
         INSERT INTO transcribes (calls_id, transcribe)
         VALUES ($callId, $transcribe) RETURNING *
       """
      .query[CreateTranscribeResp]
      .unique
      .transact(dbConnect)

  def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]] =
    sql"""
         SELECT * FROM calls
         WHERE ani=$ani
           AND date >= $dateFrom
       """
      .query[CallInfo]
      .to[List]
      .transact(dbConnect)
}

object Logics {
  type HasLogicsClient = Has[Service]

  @accessible
  trait Service {
    def createCallPBX(externalCallId: String, ani: String): Task[Long]
    def createTranscribe(callId: Long, transcribe: String): Task[CreateTranscribeResp]
    def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]]
  }

  val live: URLayer[DbTransactor, HasLogicsClient] =
    ZLayer.fromService { resource =>
      new Logics(resource.dbConnect)
    }
}

