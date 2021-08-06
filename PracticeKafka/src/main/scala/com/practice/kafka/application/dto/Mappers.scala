package com.practice.kafka.application.dto

import com.practice.kafka.domain.Models.Person

object Mappers {

  def domainPerson2DTO(person:Person):DTOS.Person={
    DTOS.Person(person.name,person.lastName,person.year)
  }

  def dtoPerson2Domain(person:DTOS.Person):Person={
    Person(person.name,person.lastName,person.year)
  }

}
