package apiservice.calls

import apiservice.calls.Model.{CallInfo, CallsInfoResp, CreateTranscribeResp}
import doobie._
import doobie.implicits._
import zio._
import zio.interop.catz._
import org.joda.time.DateTime
import doobie.implicits.javasql._

import java.sql.Timestamp
import java.time.Instant


object Logics {

  implicit val zioRuntime: zio.Runtime[zio.ZEnv] = zio.Runtime.default

  def dbConnect: Transactor[Task] =
    Transactor.fromDriverManager[Task](
      "org.postgresql.Driver",
      "jdbc:postgresql://localhost:5432/testDB",
      "postgres",
      "postgres",
    )


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


  def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]] = {
//    val dateFromTs: Timestamp = new Timestamp(dateFrom.millisOfDay.withMinimumValue.getMillis)
//    val dateToTs =  new Timestamp(dateTo.getOrElse(dateFrom).millisOfDay.withMaximumValue.getMillis)

    sql"""
         SELECT * FROM calls
         WHERE ani=$ani
           AND date BETWEEN $dateFrom AND $dateTo
       """
      .query[CallInfo]
      .to[List]
      .transact(dbConnect)
  }
}

