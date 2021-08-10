package com.practice.cache.testinterpreters

import cats.Applicative
import cats.data.EitherT
import com.practice.cache.domain.{Entities, PersonAlgebra}

class PersonAlgebraTestInterpreter [F[_]:Applicative] extends PersonAlgebra[F]{
  override def filterUser(name: String, exactFilter: Boolean, people: List[Either[String, Entities.Person]]): EitherT[F, String, List[Entities.Person]] = {
    val response: Either[String, List[Entities.Person]] = Right(people.filter(_.isRight).map(_.right.get))

    EitherT.fromEither(response)
  }
}
