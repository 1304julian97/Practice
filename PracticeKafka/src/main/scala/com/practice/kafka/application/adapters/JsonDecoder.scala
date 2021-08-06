package com.practice.kafka.application.adapters

import com.practice.kafka.application.dto.DTOS
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

trait JsonDecoder {

  implicit val personDecoder: Decoder[DTOS.Person] = deriveDecoder[DTOS.Person]
  implicit val personEncoder: Encoder[DTOS.Person] = deriveEncoder[DTOS.Person]

  implicit val responsePeopleDecoder: Decoder[DTOS.ResponsePeople] = deriveDecoder[DTOS.ResponsePeople]
  implicit val responsePeopleEncoder: Encoder[DTOS.ResponsePeople] = deriveEncoder[DTOS.ResponsePeople]

}
