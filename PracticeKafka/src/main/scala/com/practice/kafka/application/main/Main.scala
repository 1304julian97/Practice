package com.practice.kafka.application.main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.practice.kafka.application.adapters.{HttpServer, KafkaConsumer}
import com.practice.kafka.infraestructure.{Context, ContextInH2}
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}



object Main extends App with HttpServer {

  private val config: Config = ConfigFactory.load()
  private val appName  = config.getString("app.name")

  implicit val system: ActorSystem = ActorSystem( "HttpAdapter" )
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val akkaHost = config.getString("akka.host")
  private val akkaPort = config.getInt("akka.port")

  println(appName)
  println(akkaPort)
  println(akkaHost)
  println("main1")


  implicit val context:Context  = new ContextInH2

  KafkaConsumer.consumerPostPerson("person-topic")

  val server: Future[Http.ServerBinding] = Http().bindAndHandle( route, akkaHost, akkaPort )

  server.onComplete {
    case Success( Http.ServerBinding( localAddress ) ) =>
      println( s"Server online at ${localAddress.getAddress}:${localAddress.getPort}" )
    case Failure( ex ) =>
      println( s"There was an error while starting server", ex )
  }

  // PostStop
  sys.addShutdownHook {
    println( "PostStopKafka" )
  }
  println("fin")

}
