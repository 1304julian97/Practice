package com.practice.cache.controllers

import cats.data.EitherT
import cats.{FlatMap, Monad}
import cats.effect.Sync
import com.practice.cache.domain.{Entities, PersonService}
import org.http4s.{Header, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.util.CaseInsensitiveString
import cats.syntax.all._
import com.practice.cache.controllers.DTOS.{BadResponse, ResponsePeople}

class PersonController[F[_]:Sync] (implicit M: Monad[F])extends Http4sDsl[F] with JsonDecoders {


  private def filterUserByName(personService:PersonService[F]):HttpRoutes[F]={
    val dsl = new Http4sDsl[F]{}
    object ExactNameOptionQueryParam extends OptionalQueryParamDecoderMatcher[Boolean]("isExactly")

    //import dsl._
    HttpRoutes.of[F] {
      case req @ GET -> Root / "person" / name :? ExactNameOptionQueryParam(exactFilter) =>
        val token: Option[Header] = req.headers.get(CaseInsensitiveString("token"))

        val people: EitherT[F, String, List[Entities.Person]] = for {
          peopleListEither <- personService.filterUserByName(name, exactFilter.getOrElse(false))
          _ <- EitherT.fromOption(token,"Token does not exist")
        }yield peopleListEither//peopleListFilter


      val f: F[Either[String, List[Entities.Person]]] =  people.value

        people.value.flatMap{
          case Right(peopleList) => Ok(ResponsePeople(peopleList.map(DTOSConverter.entitiePerson2DTOPerson)))
          case Left(errorMessage) => NotFound(BadResponse(errorMessage))
        }


    }

  }

  def endpoints(userService: PersonService[F]): HttpRoutes[F] = {
    //To convine routes use the function `<+>`
    filterUserByName(userService)
  }
}

object PersonController{
  def endpoints[F[_]: Sync](userService: PersonService[F]): HttpRoutes[F] =
    new PersonController[F].endpoints(userService)
}
