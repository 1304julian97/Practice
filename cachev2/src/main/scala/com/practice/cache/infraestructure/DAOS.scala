package com.practice.cache.infraestructure

object DAOS {

  final case class Person(name:String,lastName:String,year:Int)
  final case class ListPeople(people:List[Person], date:String)

  final case class ResponseGetPeopleError(message:String)


  case class ResponseGetPeopleErrorV2(error:Throwable) extends Throwable

}
