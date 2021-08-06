
name := "Practice"

version := "0.1"

scalaVersion := "2.12.4"

val kafkaSerializationV = "0.1.23"

libraryDependencies ++= Seq(
  //"org.typelevel" %% "cats-core" % "2.1.1",
  "com.typesafe" % "config" % "1.4.0",
  "org.scalatest" %% "scalatest" % "3.1.2" % "test",
  "org.scalacheck" %% "scalacheck" % "1.14.1" % "test",
  "eu.timepit" %% "refined-scalacheck" % "0.9.24" % "test",
  "io.monix" %% "monix" % "3.3.0",
  //"io.estatico" %% "newtype" % "0.4.4",
  //"eu.timepit" %% "refined" % "0.9.24",
  //"eu.timepit" %% "refined-cats" % "0.9.24",
  "org.apache.kafka" %% "kafka" % "2.6.2",
  "io.circe" %% "circe-core" % "0.9.3",
  "io.circe" %% "circe-generic" % "0.9.3",
  "io.circe" %% "circe-java8" % "0.9.3",
  "io.circe" %% "circe-parser" % "0.9.3",
  "com.h2database" % "h2" % "1.4.189",
  "com.typesafe.akka" %% "akka-http" % "10.1.1",
  "com.typesafe.slick" %% "slick" % "3.3.0",
  "de.heikoseeberger" %% "akka-http-circe" % "1.20.1",

)



lazy val core       = project.in( file( "." ) )

lazy val PracticeKafka = project
  .settings(
    name := "Kafka-consumer",
    mainClass in Compile := Option("com.practice.kafka.application.main.Main"),
  ).dependsOn(core)


lazy val scaffeine2  = project.in( file( "cachev2" ) ).
  settings(
    name := "cache-implementation"
  )


addCommandAlias( name = "compile-kafkaConsumer", value = "PracticeKafka/compile;" )
addCommandAlias( name = "run-kafkaConsumer", value = "PracticeKafka/run;" )
addCommandAlias( name = "clean-cachev2", value = "cachev2/clean;" )
addCommandAlias( name = "compile-cachev2", value = "cachev2/compile;" )
