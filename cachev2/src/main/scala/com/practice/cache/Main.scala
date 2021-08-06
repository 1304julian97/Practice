package com.practice.cache

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Resource, Timer}
import cats.implicits.{catsSyntaxApplicativeId, toSemigroupKOps}
import com.practice.cache.config.ConfigsEntities.ServerConfig
import com.practice.cache.controllers.PersonController
import com.practice.cache.domain.cachesettings.CaffeineCacheImplementation
import com.practice.cache.domain.interpreters.{PersonAlgebraInterpreter, PersonRepositoryAlgebraInterpreter, PersonRepositoryAlgebraInterpreterHTTP}
import com.practice.cache.domain.{Entities, PersonRepositoryAlgebra, PersonService}
import org.http4s.implicits.http4sKleisliResponseSyntaxOptionT
import org.http4s.server.{Router, Server}
import io.circe.config.parser
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global
import scala.concurrent.{ExecutionContext, Future}

object Main extends IOApp {



  private def server[F[_]: ContextShift: ConcurrentEffect: Timer]:Resource[F, Server[F]]  ={

    for{
      client <- BlazeClientBuilder[F](global).resource
     conf <- Resource.eval(parser.decodePathF[F, ServerConfig]("users.server"))
      personAlgebra  = new PersonAlgebraInterpreter[F]
      personRepositoryAlgebra = new PersonRepositoryAlgebraInterpreter[F]
      personRepositoryAlgebraV2 = new PersonRepositoryAlgebraInterpreterHTTP[F](client,CaffeineCacheImplementation)
      personService = new PersonService[F](personRepositoryAlgebraV2,personAlgebra)
      httpApp = (
                 PersonController.endpoints[F](personService)
                ).orNotFound
      serverExecutionContext = ExecutionContext.Implicits.global
      server  <-  BlazeServerBuilder[F](serverExecutionContext)
                 .bindHttp(conf.port, conf.host)
                  .withHttpApp(httpApp)
                 .resource
    }yield server

  }


  def run(args: List[String]) = {

    server.use(_ => IO.never).as(ExitCode.Success)//Real context
    //dcachev2Server.stream[IO].compile.drain.as(ExitCode.Success)


  }
}
