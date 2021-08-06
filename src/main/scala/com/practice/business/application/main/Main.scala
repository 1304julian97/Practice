package com.practice.business.application.main

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.practice.business.application.adapters.http.{JsonDecoders, Routes}
import com.practice.business.infraestructure.InfraModels.Producer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}
import com.practice.business.application.dto._
import io.circe.Encoder
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties
object Main extends App with Routes {

  private val config: Config = ConfigFactory.load()
  private val appName  = config.getString("app.name")

  implicit val system: ActorSystem = ActorSystem( "HttpAdapter" )
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  private val akkaHost = config.getString("akka.host")
  private val akkaPort = config.getInt("akka.port")


  //____________________________ProducerConf_______________________________
  val kafkaTopicHost  = config.getString("kafka.host")
  val kafkaTopicPort  = config.getString("kafka.port")
  val kafkaTopicAcknowledgement  = config.getString("kafka.acknowledgement")
  val kafkaTopicName = config.getString("kafka.topics.topic1")

  val propss = new Properties()
  propss.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$kafkaTopicHost:$kafkaTopicPort")
  propss.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  propss.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
  propss.put(ProducerConfig.ACKS_CONFIG, kafkaTopicAcknowledgement)
  //____________________________ProducerConf_______________________________


  override val kafkaProducerInstance = new Producer[DTOS.Person] {
    override implicit val encoder: Encoder[DTOS.Person] = personEncoder
    override val host: String = kafkaTopicHost
    override val port: String = kafkaTopicPort
    override val props: Properties = propss
    override val topic: String = kafkaTopicName
  }


  println( s"Running application $appName" )

  val server: Future[Http.ServerBinding] = Http().bindAndHandle( route, akkaHost, akkaPort )

  server.onComplete {
    case Success( Http.ServerBinding( localAddress ) ) =>
      println( s"Server online at ${localAddress.getAddress}:${localAddress.getPort}" )
    case Failure( ex ) =>
      println( s"There was an error while starting server", ex )
  }

  // PostStop
  sys.addShutdownHook {
    println( "PostStop" )
    kafkaProducerInstance.close
  }
}
