package com.practice.cache.firststepswithhttp4s

import cats.effect.Sync
import org.http4s.dsl.Http4sDsl
import org.http4s.dsl.impl.{OptionalQueryParamDecoderMatcher, QueryParamDecoderMatcher}
import org.http4s.util.CaseInsensitiveString
import org.http4s.{Header, HttpRoutes, QueryParamDecoder}

import java.time.Year
import scala.util.{Failure, Success, Try}

object dcachev2Routes {

  def jokeRoutes[F[_] : Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        val jokeTry = Try(J.get)
        println("Joke wasss__________________" + jokeTry.isFailure)
        jokeTry match {
          case Success(value) => Ok(value)
          case Failure(exception) => BadRequest(exception.getMessage + "I am exception")
        }

    }
  }

  object CountryQueryParamMatcher extends QueryParamDecoderMatcher[String]("country")

  object CityOptionalQueryParamMatcher extends OptionalQueryParamDecoderMatcher[String]("city")

  implicit val yearQueryParamDecoder: QueryParamDecoder[Year] = QueryParamDecoder[Int].map(Year.of)

  object YearQueryParamMatcher extends QueryParamDecoderMatcher[Year]("year")


  //hello/julian/carvajal?country=colombia&city=Medallo&year=1997
  def helloWorldRoutes[F[_] : Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}

    import dsl._
    HttpRoutes.of[F] {
      case req@GET -> Root / "hello" / name / name2 :? CountryQueryParamMatcher(country) :? YearQueryParamMatcher(year) :? CityOptionalQueryParamMatcher(city) =>
        val header: Option[Header] = req.headers.get(CaseInsensitiveString("Julian"))
        for {
          greeting <- H.hello(HelloWorld.Name(s"$name $name2 I am in $country in the city ${city.getOrElse("Rionegro")}, ${year.getValue}, in addition I sent this header '${header.get.value}'"))
          resp <- Ok(greeting)
        } yield resp

    }
  }
}
