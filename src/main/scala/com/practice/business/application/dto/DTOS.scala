package com.practice.business.application.dto

import java.time.LocalDateTime

object DTOS {

  case class Person(name:String,lastName:String,year:Int)

  case class ErrorResponse(message:String,date:String)

  case class SuccessResponsePostPerson(person: Person, message:String)
}
