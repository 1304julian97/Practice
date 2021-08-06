package com.practice.kafka.application.adapters

import com.practice.kafka.application.cqrs.CommandPostPerson
import com.practice.kafka.application.dto.{DTOS, Mappers}
import com.practice.kafka.domain.Models.Person
import com.practice.kafka.infraestructure.Context
import org.apache.kafka.clients.consumer.{ConsumerConfig, KafkaConsumer}

import java.time.Duration
import java.util
import java.util.Properties
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters._
import io.circe.parser.decode

object KafkaConsumer extends JsonDecoder {


  def consumerPostPerson(topic:String)(implicit context: Context): Future[Unit] = Future{
    println(Thread.currentThread().getName+"consumer1")
    println(s"Running topic: '$topic'")
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupId14544")
    props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false")

    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = consumer.poll(Duration.ofMillis(500)).asScala
      for (data <- record.iterator) {
        println("Recibí el mensaje"+data.value())
        val domainPerson = decode[DTOS.Person](data.value())  match {
          case Right(person) => Mappers.dtoPerson2Domain(person)
          case _ => Person("Soy","un Error",1997) //VALIDATE WHAT IS THE BEST WAY TO DO THAT.
        }
        import monix.execution.Scheduler.Implicits.global

        CommandPostPerson.execute(domainPerson).runToFuture


      }
    }
  }
}
