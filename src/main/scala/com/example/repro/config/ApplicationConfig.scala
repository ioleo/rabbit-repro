package com.example.repro.config

import com.typesafe.config.ConfigFactory
import zio.*
import zio.config.*
import zio.config.magnolia.*
import zio.config.syntax.*
import zio.config.typesafe.*

import ConfigDescriptor.*

final case class ApplicationConfig(
  logging: LoggingConfig,
  rabbitmq: RabbitMqConfig
)

object ApplicationConfig:

  @scala.annotation.nowarn("msg=infix")
  private val configuration =
    (
      nested("logging")(LoggingConfig.configuration) zip
        nested("rabbitmq")(descriptor[RabbitMqConfig])
    ).to[ApplicationConfig]

  private val typesafeConfig = TypesafeConfig.fromResourcePath(configuration)

  val live =
    typesafeConfig.narrow(_.logging) ++
      typesafeConfig.narrow(_.rabbitmq)
