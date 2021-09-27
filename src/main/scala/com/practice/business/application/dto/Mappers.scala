package com.practice.business.application.dto

import com.practice.business.domain.Models.{DomainError, InvalidLastName, InvalidName, InvalidYear, Person, RestrictionSizeInt, RestrictionSizeString, RestrictionSizeString2}
//import com.practice.business.domain.SintaxRefined.{RefinedInt, RefinedString}
import com.practice.business.domain.SintaxRefined.RefinedT
import cats.syntax.either._
import eu.timepit.refined.refineV

object Mappers {

  def mapPersonDTO2Domain(persondDTO:DTOS.Person):Either[DomainError,Person] = {
    /*
    Just to keep in mind, in this layer is just one option to propagate the message.
     */
    for {
      nameRefined <- persondDTO.name.validateWith[RestrictionSizeString].leftMap(InvalidName)
      lastNameRefined <- refineV[RestrictionSizeString2]("persondDTO.lastName").leftMap(_ => InvalidLastName("Invalid value in last Name"))
      yearRefined <- persondDTO.year.validateWith[RestrictionSizeInt].leftMap(InvalidYear)
    }yield Person(nameRefined,lastNameRefined,yearRefined)

  }

  def mapPersonDomain2PersonDTO(person:Person):DTOS.Person = {
    DTOS.Person(person.name.value,person.lastName.value,person.year.value)
  }

}
