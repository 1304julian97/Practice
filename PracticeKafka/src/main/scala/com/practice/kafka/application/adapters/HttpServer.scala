package com.practice.kafka.application.adapters

import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, OK}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.practice.kafka.application.cqrs.QueryGetPeople
import com.practice.kafka.application.dto.{DTOS, Mappers}
import com.practice.kafka.domain.Models.Person
import com.practice.kafka.infraestructure.Context
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import org.slf4j.LoggerFactory

import java.time.LocalDateTime
import java.util.UUID
import scala.util.{Failure, Success}

trait HttpServer extends JsonDecoder {

  val context: Context

  val route: Route =
    path("up") {
      get {
        complete(OK -> "Server Consumer Kafka up!")
      }
    } ~
      path("people") {
        get {
          val result: Task[Either[String, List[Person]]] = QueryGetPeople.execute(context)

          onComplete(result.runToFuture) {
            case Success(Right(people)) => {
              val peopleDTO: List[DTOS.Person] = people.map(Mappers.domainPerson2DTO)
              val response: DTOS.ResponsePeople = DTOS.ResponsePeople(peopleDTO, LocalDateTime.now.toString)
              complete(OK -> response)
            }
            case Success(Left(msg)) => complete(BadRequest -> msg)
            case Failure(exception) => {
              val messageId: String = UUID.randomUUID().toString
              LoggerFactory.getLogger( "Routes.class" ).error( s"Something was wrong, Use this code: $messageId", exception )
              complete(InternalServerError -> exception.getMessage)
            }
          }
        }

      }
}
