package com.practice.kafka.infraestructure

import com.practice.kafka.domain.PersonRepository

trait Context {

  val personRepository:PersonRepository

}
