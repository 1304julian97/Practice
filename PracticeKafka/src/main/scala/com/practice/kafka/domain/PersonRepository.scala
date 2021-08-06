package com.practice.kafka.domain

import com.practice.kafka.domain.Models.Person
import monix.eval.Task

trait PersonRepository {
  def getAll: Task[Either[String, List[Person]]]


  def save(person:Person): Task[Either[String,Person]]

}
