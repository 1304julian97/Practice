package com.practice.business.domain

import monix.eval.Task

object PersonService {

  def postPerson(person: Models.Person): Task[Either[String, Models.Person]] = {
    println("I am doing something")
    println(s"Person Posted '$person'")

    Task(Right(person))
  }

}
