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


  def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]] = {
    //    val dateFromTs: Timestamp = new Timestamp(dateFrom.millisOfDay.withMinimumValue.getMillis)
    //    val dateToTs =  new Timestamp(dateTo.getOrElse(dateFrom).millisOfDay.withMaximumValue.getMillis)

    sql"""
         SELECT * FROM calls
         WHERE ani=$ani
           AND date >= $dateFrom
       """
      .query[CallInfo]
      .to[List]
      .transact(dbConnect)
  }

  //List[String]=======================================================================================

  //
  //  type NewTranscribe = (Int, String)
  //
  //  def createTranscribe(calls_id: Int, transcribe: List[String]): Task[Int] = {
  //
  //    val listTranscribe: List[NewTranscribe] = transcribe.map(x => new NewTranscribe(calls_id, x))
  //
  //    val sql = "INSERT INTO transcribes (calls_id, transcribe) values (?, ?)"
  //    Update[NewTranscribe](sql)
  //      .updateMany(listTranscribe)
  //      .transact(xa)
  //  }

}

//@accessible
object Logics {
  type HasLogicsClient = Has[Service]

  @accessible
  trait Service {
    def createCallPBX(externalCallId: String, ani: String): Task[Long]
    def createTranscribe(callId: Long, transcribe: String): Task[CreateTranscribeResp]
    def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]]
  }

  //@accessible макрол для генерации таких методов ставится над object Logic:
  //-добавить zio-marcos в зависимости
  //-scalaOptions -Ymacro...
  //RIO[HasLogicsClient, Long]  - Long возвращаем
//  def createCallPBX111(externalCallId: String, ani: String): RIO[HasLogicsClient, Long] =
//    RIO.accessM[HasLogicsClient](_.get.createCallPBX(externalCallId, ani))
//
//  def createTranscribe111(callId: Long, transcribe: String): RIO[HasLogicsClient, CreateTranscribeResp] =
//    RIO.accessM[HasLogicsClient](_.get.createTranscribe(callId, transcribe))
//
//  def getCalls111(ani: String, dateFrom: Instant, dateTo: Option[Instant]): RIO[HasLogicsClient, List[CallInfo]] =
//    RIO.accessM[HasLogicsClient](_.get.getCalls(ani, dateFrom, dateTo))

  val live: URLayer[DbTransactor111, HasLogicsClient] =
    ZLayer.fromService { resource =>
      new Logics(resource.dbConnect)
    }
}

