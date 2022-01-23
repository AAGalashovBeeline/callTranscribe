package apiservice.calls.repository

import apiservice.calls.Model.{CallInfo, CreateTranscribeResp}
import apiservice.calls.transactor.DbTransactor
import doobie._
import doobie.implicits._
import zio._
import zio.interop.catz._

import java.time.Instant

class CallsRepository(dbConnect: Transactor[Task]) extends CallsRepository.Service {

  def createCallPBX(externalCallId: String, ani: String): Task[Long] =
    sql"""
        INSERT INTO CALLS ("external_callId", ani)
        VALUES ($externalCallId, $ani) RETURNING id
       """
      .query[Long]
      .unique
      .transact(dbConnect)      //transactor.Transactor


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


object CallsRepository {
  type HasLogicsClient = Has[Service]

  trait Service {
    def createCallPBX(externalCallId: String, ani: String): Task[Long]
    def createTranscribe(callId: Long, transcribe: String): Task[CreateTranscribeResp]
    def getCalls(ani: String, dateFrom: Instant, dateTo: Option[Instant]): Task[List[CallInfo]]
  }

  //val live: ZLayer[HasConfig with HasHttpClient, Throwable, HasLogicsClient] =
//  val live: ZLayer[HasConfig, Throwable, HasLogicsClient] =
//    (for {
//      cfg <- AppConfig.get
//      //client <- ZIO.service[HttpClient.Service]
//    } yield new Logics(cfg)).toLayer



  val live: URLayer[DbTransactor, HasLogicsClient] =
    ZLayer.fromService { resource =>
      new CallsRepository(resource.dbConnect)
    }


//  implicit val zioRuntime: zio.Runtime[zio.ZEnv] = zio.Runtime.default
//
//  def dbConnect: Transactor[Task] =
//    Transactor.fromDriverManager[Task](
//      "org.postgresql.Driver",
//      "jdbc:postgresql://localhost:5432/testDB",
//      "postgres",
//      "postgres",
//    )

}

