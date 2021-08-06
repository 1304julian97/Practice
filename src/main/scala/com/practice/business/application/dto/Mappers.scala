package com.practice.business.application.dto

import com.practice.business.domain.Models.Person

object Mappers {

  def mapPersonDTO2Domain(persondDTO:DTOS.Person):Either[String,Person] = {
    Person(persondDTO.name,persondDTO.lastName,persondDTO.year)
  }

  def mapPersonDomain2PersonDTO(person:Person):DTOS.Person = {
    DTOS.Person(person.name,person.lastName,person.year)
  }

}
