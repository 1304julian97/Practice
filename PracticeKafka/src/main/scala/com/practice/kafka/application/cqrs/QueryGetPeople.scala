package com.practice.kafka.application.cqrs

import com.practice.kafka.domain.{Models, PersonService}
import com.practice.kafka.infraestructure.Context
import monix.eval.Task

object QueryGetPeople {
  def execute(implicit context:Context): Task[Either[String, List[Models.Person]]] = {
    PersonService.getPeople
  }

}
