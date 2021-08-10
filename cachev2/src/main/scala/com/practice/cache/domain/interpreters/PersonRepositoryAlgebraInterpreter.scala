package com.practice.cache.domain.interpreters

import cats.{Applicative, Id}
import cats.implicits.catsSyntaxApplicativeId
import com.practice.cache.domain.Entities.Person
import com.practice.cache.domain.{Entities, PersonRepositoryAlgebra}

class PersonRepositoryAlgebraInterpreter[F[_]:Applicative] extends PersonRepositoryAlgebra[F] {
  override def findAllPeople: F[List[Either[String,Person]]]= {
    val peopleFound: List[Either[String, Person]] = List(Person("Julian","Carvajal",1997)).filter(_.isRight)
    peopleFound.pure
  }
}
