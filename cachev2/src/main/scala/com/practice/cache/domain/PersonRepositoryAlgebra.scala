package com.practice.cache.domain

import cats.data.EitherT
import com.practice.cache.domain.Entities.Person

trait PersonRepositoryAlgebra[F[_]] {
  def findAllPeople :F[List[Either[String,Person]]]//F[List[Person]]

}
