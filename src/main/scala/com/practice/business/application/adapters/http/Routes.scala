package com.practice.business.application.adapters.http

import akka.http.scaladsl.model.StatusCodes.{BadRequest, InternalServerError, OK}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.practice.business.application.cqrs.CommandPostPerson
import com.practice.business.application.dto.DTOS.{ErrorResponse, SuccessResponsePostPerson}
import com.practice.business.application.dto.{DTOS, Mappers}
import com.practice.business.domain.Models.Person
import com.practice.business.infraestructure.InfraModels.Producer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.slf4j.LoggerFactory

import java.time.LocalDateTime
import java.util.UUID
import scala.util.{Failure, Success}


trait Routes extends JsonDecoders{


  val kafkaProducerInstance: Producer[DTOS.Person]

  val route: Route =
    path("up"){
      get{
        complete(OK -> "Server up!")
      }
    }~
      path("person"){
        post{
          entity( as[DTOS.Person]){ request =>

            val response: Task[Either[String,Person]] = Mappers.mapPersonDTO2Domain(request) match {
              case Left(errorMessage) => Task(Left(errorMessage))
              case Right(person) => CommandPostPerson.execute(person)(kafkaProducerInstance)
            }

            onComplete(response.runToFuture) {
              case Success(Left(errorMessage)) => complete(BadRequest -> ErrorResponse(errorMessage, LocalDateTime.now().toString))
              case Success(Right(person)) => complete(OK -> SuccessResponsePostPerson(Mappers.mapPersonDomain2PersonDTO(person),"Person posted correctly"))
              case Failure(exception) => {
                val messageId: String = UUID.randomUUID().toString
                LoggerFactory.getLogger( "Routes.class" ).error( s"Something was wrong, Use this code: $messageId", exception )
                complete(InternalServerError -> ErrorResponse(exception.getMessage,LocalDateTime.now().toString))
              }
            }


          }
        }
      }


}
