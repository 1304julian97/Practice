package com.practice.kafka.application.cqrs

import com.practice.kafka.domain.Models.Person
import com.practice.kafka.infraestructure.Context
import monix.eval.Task

object CommandPostPerson {

  def execute(person:Person)(implicit context:Context):Task[Unit]=Task{

    context.personRepository.save(person)
    println(s"Hello!! I recieve $person")

  }

}
