package com.practice.cache.domain

import java.time.LocalDate

object Entities {


  final case class Person(name:String,lastName:String,year:Int)

  object Person{
    def apply(name:String,lastName:String,year:Int):Either[String,Person] = {
      if(year > LocalDate.now().getYear) Left(s"The year of the person $name $lastName must be smaller than the current year")
      else Right(new Person(name,lastName,year))
    }
  }

}
