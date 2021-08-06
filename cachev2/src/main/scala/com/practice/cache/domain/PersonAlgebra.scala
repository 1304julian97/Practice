package com.practice.cache.domain

import cats.data.EitherT
import com.practice.cache.domain.Entities.Person

trait PersonAlgebra[F[_]] {
  def filterUser(name:String, exactFilter: Boolean, people: List[Either[String,Person]]) :EitherT[F,String,List[Person]]

}
