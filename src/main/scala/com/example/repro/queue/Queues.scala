package com.example.repro.queue

import cats.effect.std.{Console => CEConsole}
import com.example.repro.config.RabbitMqConfig
import com.comcast.ip4s.*
import lepus.client.*
import lepus.protocol.domains.*
import zio.*
import zio.interop.catz.*

object Queues:

  val rabbitClient: ZLayer[RabbitMqConfig, Throwable, Connection[Task]] = ZLayer.scoped {
    for
      config               <- ZIO.service[RabbitMqConfig]
      given CEConsole[Task] = CEConsole.make[Task]
      connection           <- LepusClient[Task](
                                host = Host.fromString(config.host).get,
                                port = Port.fromInt(config.port).get,
                                username = config.username,
                                password = config.password,
                                vhost = Path("/"),
                                config = ConnectionConfig.default,
                                debug = false
                              ).toScopedZIO
    yield connection
  }
