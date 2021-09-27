package com.practice.business.domain

import eu.timepit.refined.auto.autoRefineV
import io.estatico.newtype.macros.newtype
import eu.timepit.refined.boolean.And
import eu.timepit.refined.{refineMV, refineV}
import shapeless.Witness

import java.time.{LocalDate, LocalDateTime}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.{MaxSize, MinSize}
import eu.timepit.refined.numeric.{Greater, Less, Positive}


object Models {

  //@newtype case class Name(value: NonEmptyString)
  //@newtype case class Name(value: String)

  type RestrictionSizeString  = MinSize[Witness.`5`.T] And MaxSize[Witness.`20`.T]
  type RestrictionSizeString2  = MinSize[Witness.`2`.T] And MaxSize[Witness.`10`.T]
  type LastName = String Refined RestrictionSizeString2
  type RestrictionSizeInt = (Positive And Greater[Witness.`1997`.T])
  type Year  = Int Refined RestrictionSizeInt
  type Name = String Refined RestrictionSizeString



  final case class Person(name:Name,lastName:LastName,year:Year)

  //final case class Person(name: String, lastName: String, year: Int)

/*  object Person {

    //Person(Name(Name("Julian")),"Carvajal",1997)
    //val rifdskf: Either[String, LastNameRest] = refineV[Restriction]("Carvajal")

    def apply(name: String, lastName: String, year: Int): Either[String, Person] = {
      if (year > LocalDateTime.now().getYear) Left(s"The year $year must be smaller than current.")
      else Right(new Person(Name(name), "lastName", year))

    }

  }*/
  trait DomainError

  case class InvalidName(message:String) extends DomainError
  case class InvalidLastName(message:String) extends DomainError
  case class InvalidYear(message:String) extends DomainError




}
