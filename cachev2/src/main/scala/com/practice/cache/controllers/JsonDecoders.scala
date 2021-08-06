package com.practice.cache.controllers

import com.practice.cache.controllers.DTOS.{BadResponse, Person, ResponsePeople}
import io.circe.generic.semiauto.deriveEncoder
import io.circe.{Encoder, Json}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

import java.time.LocalDate

trait JsonDecoders {


  implicit val personEncoder: Encoder[Person] = new Encoder[Person] {
    final def apply(p: Person): Json = Json.obj(
      ("name", Json.fromString(p.name)),
      ("lastName", Json.fromString(p.lastName)),
      ("year", Json.fromInt(p.year)),
      ("dateTrace", Json.fromString(LocalDate.now().toString)),
    )
  }
  implicit def personEntityEncoder[F[_]]: EntityEncoder[F, Person] = jsonEncoderOf[F, Person]


  implicit val peopleResponseEncoder: Encoder[ResponsePeople] = deriveEncoder[ResponsePeople]
  implicit def peopleResponseEntityEncoder[F[_]]: EntityEncoder[F, ResponsePeople] = jsonEncoderOf[F, ResponsePeople]



  implicit val badResponseEncoder: Encoder[BadResponse] = deriveEncoder[BadResponse]
  implicit def badResponseEntityEncoder[F[_]]: EntityEncoder[F, BadResponse] = jsonEncoderOf[F, BadResponse]



}
