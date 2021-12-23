//package apiservice.calls
//
//import createTranscribecreateTranscribeapiservice.calls.Model.CallsTest
//import cats.effect._
//import cats.effect.unsafe.implicits.global
//import doobie._
//import doobie.implicits._
//
//object LogicOld {
//
//  val connect = Transactor.fromDriverManager[IO](
//    "org.postgresql.Driver",
//    "jdbc:postgresql://localhost:5432/backend",
//    "postgres",
//    "postgres",
//  )
//
//
//
//  def getCalls: Seq[CallsTest] = sql"""select id,ani from call"""
//    .query[CallsTest]    // Query0[String]
//    .to[List]         // ConnectionIO[List[String]]
//    .transact(connect)     // IO[List[String]]
//    .unsafeRunSync()    // List[String]
//    .take(5)          // List[String]
//}
