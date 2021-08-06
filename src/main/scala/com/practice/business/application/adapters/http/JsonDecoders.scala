package com.practice.business.application.adapters.http

import com.practice.business.application.dto.DTOS
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

trait JsonDecoders {

  implicit val personDecoder: Decoder[DTOS.Person] = deriveDecoder[DTOS.Person]
  implicit val personEncoder: Encoder[DTOS.Person] = deriveEncoder[DTOS.Person]


  implicit val errorResponseDecoder: Decoder[DTOS.ErrorResponse] = deriveDecoder[DTOS.ErrorResponse]
  implicit val errorResponseEncoder: Encoder[DTOS.ErrorResponse] = deriveEncoder[DTOS.ErrorResponse]


  implicit val successResponseDecoder: Decoder[DTOS.SuccessResponsePostPerson] = deriveDecoder[DTOS.SuccessResponsePostPerson]
  implicit val successResponseEncoder: Encoder[DTOS.SuccessResponsePostPerson] = deriveEncoder[DTOS.SuccessResponsePostPerson]

}
