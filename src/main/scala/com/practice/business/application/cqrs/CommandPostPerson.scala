package com.practice.business.application.cqrs

import cats.data.EitherT
import com.practice.business.application.adapters.kafka.KafkaProducer
import com.practice.business.application.dto.{DTOS, Mappers}
import com.practice.business.domain.{Models, PersonService}
import com.practice.business.infraestructure.InfraModels.Producer
import com.typesafe.config.{Config, ConfigFactory}
import monix.eval.Task

object CommandPostPerson {

  def execute(person: Models.Person)(producer: Producer[DTOS.Person]): Task[Either[String, Models.Person]] = {

    val config: Config = ConfigFactory.load()
    val topic = config.getString("kafka.topics.topic1")

    val producerFunction: DTOS.Person => Task[Either[String, Unit]] = person => Task(Right(producer.post(person.name)(person)))
    //val producerFunction: DTOS.Person => Task[Either[String, Unit]] = person => Task(Right(KafkaProducer.postPerson(topic)(person)))
    val mapperFunciont: Models.Person => Task[Either[String, DTOS.Person]] = person => Task(Right(Mappers.mapPersonDomain2PersonDTO(person)))

    val finalResponse = for {
      person <- EitherT(PersonService.postPerson(person))
      personDTO <- EitherT(mapperFunciont(person))
      _ <- EitherT(producerFunction(personDTO))
    } yield person

    finalResponse.value
  }

}

