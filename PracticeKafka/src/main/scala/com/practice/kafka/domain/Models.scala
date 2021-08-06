package com.practice.kafka.domain

import java.time.LocalDateTime

object Models {

  final case class Person(name:String,lastName:String,year:Int)
/*
  object Person{
    def apply(name:String,lastName:String,year:Int):Either[String, Person] = {
      if(year > LocalDateTime.now().getYear) Left(s"The year $year must be smaller than current.")
      else Right(new Person(name, lastName, year))
    }
  }*/

}
