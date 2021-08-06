package com.practice.kafka.domain

import com.practice.kafka.infraestructure.Context
import monix.eval.Task

object PersonService {
  def getPeople(implicit context:Context): Task[Either[String, List[Models.Person]]] = {
    context.personRepository.getAll
  }

}
