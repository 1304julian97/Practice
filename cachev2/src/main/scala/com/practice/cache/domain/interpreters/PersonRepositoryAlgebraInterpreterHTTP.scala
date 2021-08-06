package com.practice.cache.domain.interpreters

import cats.{Applicative, Monad}
import cats.effect.Async
import cats.implicits.catsSyntaxApplicativeId
import com.practice.cache.domain.cachesettings.Cache
import com.practice.cache.domain.{Entities, PersonRepositoryAlgebra}
import com.practice.cache.infraestructure.{DAOS, DAOSConverter, JsonDecoders}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.dsl.io.GET
import org.http4s.implicits.http4sLiteralsSyntax

import scala.concurrent.duration.DurationInt


class PersonRepositoryAlgebraInterpreterHTTP[F[_] : Async](C: Client[F], cache: Cache[Int, List[Entities.Person]])(implicit M: Monad[F]) extends PersonRepositoryAlgebra[F] with JsonDecoders {


  override def findAllPeople: F[List[Either[String, Entities.Person]]] = {

    val dsl = new Http4sClientDsl[F] {}
    import dsl._

    val keyValueCache = 1

    val peopleFunction: Entities.Person => Either[String, Entities.Person] = p => Right(p)
    cache.get(keyValueCache) match {
      case Some(people) =>
        println("USING CACHE :)")
        people.map(peopleFunction).pure
      case _ =>
        println("CACHÉ DOES NOT EXIST :(")
        val httpResponse = GET(uri"http://localhost:8081/people")
        val mappedResponse: F[DAOS.ListPeople] = C.expect[DAOS.ListPeople](httpResponse)

        val finalResponseFunction: DAOS.ListPeople => List[Either[String, Entities.Person]] = people => people.people.map(DAOSConverter.personDAO2Domain)
        M.map(mappedResponse) { list =>
          val response: List[Either[String, Entities.Person]] = finalResponseFunction(list)

          val (messages, peopleFiltered) = response.foldRight[(List[String],List[Entities.Person])](Nil,Nil) {
            case (Left(message), (leftAcumulated,rightAcumulated)) => (message :: leftAcumulated,rightAcumulated)
            case (Right(person),(leftAcumulated,rightAcumulated)) => (leftAcumulated, person :: rightAcumulated)
          }

          if(messages.isEmpty) println("No problems found getting people")
          else println(s"Problems found getting people: ${messages.mkString(",")}")

          //val peopleRight = response.filter(_.isRight)
          if (peopleFiltered.nonEmpty) {
            println("CREATING CACHE :D")
            cache.save(keyValueCache,peopleFiltered, Some(15.seconds))
          }
          response
        }

    }


  }
}
