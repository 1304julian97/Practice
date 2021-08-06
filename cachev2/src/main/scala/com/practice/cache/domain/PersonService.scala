package com.practice.cache.domain

import cats.Monad
import cats.data.EitherT
import cats.effect.Async
import com.practice.cache.domain.Entities.Person

import java.time.LocalTime
import java.time.temporal.ChronoUnit.MILLIS


class PersonService[F[_]:Async](personRepositoryAlgebra: PersonRepositoryAlgebra[F], personAlgebra: PersonAlgebra[F]) {
  def filterUserByName(name:String, exactFilter: Boolean)(implicit M: Monad[F]):EitherT[F,String,List[Person]] ={

    EitherT(
      M.flatMap{
        val initialTime = LocalTime.now()
        val response = personRepositoryAlgebra.findAllPeople
        val finalTime = LocalTime.now()
        println("Time when it finished: "+ MILLIS.between(initialTime,finalTime)+ " Milliseconds")
        response
      } {
        people => personAlgebra.filterUser(name, exactFilter, people).value
      }
    )



  }

}
