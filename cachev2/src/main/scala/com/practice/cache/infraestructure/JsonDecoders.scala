package com.practice.cache.infraestructure

import cats.effect.Sync
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{jsonEncoderOf, jsonOf}

trait JsonDecoders {

  implicit val personDecoder: Decoder[DAOS.Person] = deriveDecoder[DAOS.Person]
  implicit val personEncoder: Encoder[DAOS.Person] = deriveEncoder[DAOS.Person]


  implicit val ListPeopleDecoder: Decoder[DAOS.ListPeople] = deriveDecoder[DAOS.ListPeople]
  implicit val ListPeopleEncoder: Encoder[DAOS.ListPeople] = deriveEncoder[DAOS.ListPeople]



  implicit def listPeopleEntityDecoder[F[_]: Sync]: EntityDecoder[F, DAOS.ListPeople] = jsonOf


  implicit def listPeopleEntityEncoder[F[_]]: EntityEncoder[F, DAOS.ListPeople] =  jsonEncoderOf



/*  implicit def ListPeopleEntityEncoder[F[_]]:EntityEncoder[F,DAOS.ListPeople] = jsonEncoderOf[F,DAOS.ListPeople]
  implicit def ListPeopleEntityDecoder[F[_]: Sync]: EntityDecoder[F, DAOS.ListPeople] = jsonOf


  implicit def PersonEntityDecoder[F[_]: Sync]: EntityDecoder[F, DAOS.Person] = jsonOf
  implicit def personEntityEncoder[F[_]]:EntityEncoder[F,DAOS.Person] = jsonEncoderOf[F,DAOS.Person]*/

}
