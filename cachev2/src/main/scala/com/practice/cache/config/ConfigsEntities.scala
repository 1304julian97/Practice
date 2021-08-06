package com.practice.cache.config

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf

object ConfigsEntities {


  final case class ServerConfig(host:String, port:Int)

  final case class CaffeineConfig(timeToLeave:Int)

  //final case class Config(server: ServerConfig)

  object ServerConfig{


    implicit val peopleResponseEncoder: Encoder[ServerConfig] = deriveEncoder[ServerConfig]
    implicit val peopleResponseDecoder: Decoder[ServerConfig] = deriveDecoder[ServerConfig]
    implicit def peopleResponseEntityEncoder[F[_]]: EntityEncoder[F, ServerConfig] = jsonEncoderOf[F, ServerConfig]
  }

  object CaffeineConfig{
    implicit val caffeineConfigEncoder: Encoder[CaffeineConfig] = deriveEncoder[CaffeineConfig]
    implicit val caffeineConfigDecoder: Decoder[CaffeineConfig] = deriveDecoder[CaffeineConfig]
    implicit def caffeineConfigEntityEncoder[F[_]]: EntityEncoder[F, CaffeineConfig] = jsonEncoderOf[F, CaffeineConfig]
  }

}

