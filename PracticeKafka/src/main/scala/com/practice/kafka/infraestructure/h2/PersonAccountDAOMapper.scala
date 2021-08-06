package com.practice.kafka.infraestructure.h2

import com.practice.kafka.domain.Models.Person

trait PersonAccountDAOMapper {

  def personDAO2Domain(person:PersonDAORecord):Person = {
    Person(person.name,person.lastName, person.year)
  }

  def personDomain2DAO(person:Person):PersonDAORecord = {
    PersonDAORecord(person.name, person.lastName,person.year)
  }

}
