package com.practice.business.application.adapters.kafka

import com.practice.business.application.adapters.http.JsonDecoders
import com.practice.business.application.dto.DTOS.Person
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import io.circe.syntax._
import java.util.Properties


object KafkaProducer extends JsonDecoders{

  def postPerson(topic: String)(person: Person): Unit = {

    val config: Config = ConfigFactory.load()
    val kafkaTopicHost  = config.getString("kafka.host")
    val kafkaTopicPort  = config.getString("kafka.port")
    val kafkaTopicAcknowledgement  = config.getString("kafka.acknowledgement")

    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, s"$kafkaTopicHost:$kafkaTopicPort")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer")
    props.put(ProducerConfig.ACKS_CONFIG, kafkaTopicAcknowledgement)

    val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](props)

    val record = new ProducerRecord[String, String](topic, "julian",     person.asJson.noSpaces)
    val messageSent = producer.send(record)
    messageSent.get().offset()
    producer.close()
  }

}

