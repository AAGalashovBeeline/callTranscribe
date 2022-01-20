name := "test"

version := "0.1"

scalaVersion := "2.13.7"

lazy val doobieVersion = "1.0.0-RC1"

lazy val tapirVersion = "0.18.1"         //18-01 0.20.0-M5 error

lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.10",
      "io.d11" %% "zhttp" % "1.0.0.0-RC17",


      "io.circe" %% "circe-generic" % "0.14.1",


      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-zio-http" % tapirVersion,

      "com.softwaremill.sttp.tapir" %% "tapir-refined" % tapirVersion,
      "com.softwaremill.sttp.tapir" %%"tapir-swagger-ui-zio-http" % tapirVersion,


      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,

      "dev.zio" %% "zio-interop-cats" % "3.0.2.0",

      "joda-time" % "joda-time" % "2.10.10",

      "com.github.pureconfig" %% "pureconfig" % "0.16.0"
    ),
  )