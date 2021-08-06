package com.practice.kafka.application.dto

object DTOS {

  case class Person(name:String,lastName:String,year:Int)

  case class ResponsePeople(people: List[Person], date:String)

}
