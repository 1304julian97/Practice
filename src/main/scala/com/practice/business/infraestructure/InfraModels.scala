package com.practice.business.infraestructure

import io.circe.Encoder
import io.circe.syntax._
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}

import java.util.Properties

object InfraModels {

  trait Producer[A] {

    implicit val encoder: Encoder[A]

    val host: String
    val port: String
    val props: Properties
    val topic: String

    private lazy val producer = new KafkaProducer[String, String](props)

    def post(key: String)(obj: A): Unit = {
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$host:$port")
      val record = new ProducerRecord[String, String](topic, key, obj.asJson.noSpaces)
      producer.send(record)
      //producer.close()
    }

    def close(): Unit = producer.close()
  }

}
