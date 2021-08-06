package com.practice.cache.controllers

import com.practice.cache.controllers.DTOS.Person
import com.practice.cache.domain.Entities

object DTOS {

  case class ResponsePeople(people:List[Person])

  case class BadResponse(message:String)

  case class Person(name:String,lastName:String, year:Int)
}

object DTOSConverter{

  def entitiePerson2DTOPerson(person: Entities.Person):Person = {
    Person(person.name,person.lastName,person.year)
  }
}
