package com.example.repro

import com.example.repro.config.*
import com.example.repro.process.*
import com.example.repro.queue.*
import zio.*
import zio.logging.backend.SLF4J
import zio.schema.codec.*

object Application extends ZIOAppDefault:

  lazy val program =
    ZIO.service[LoggingConfig].flatMap { logCfg =>
      logCfg.level {
        (for
          _ <- ZIO.logInfo("Application starting...")
          _ <- ZIO.serviceWithZIO[EventRoundTripDaemonRabbitMq](_.start()).fork
          _ <- ZIO.never
        yield ())
          .onInterrupt(ZIO.logInfo("Gracefully shutting down..."))
          .catchAllDefect { case defect =>
            ZIO.logFatal(s"Shutting down due to fatal defect:\n$defect")
          }
          .absorb
          .catchAllTrace { case (_, trace) =>
            val message = trace.prettyPrint
            ZIO.logFatal(s"Shutting down due to fatal defect:\n$message")
          }
      }
    }

  override val run =
    program.provide(
      ApplicationConfig.live,
      EventRoundTripDaemon.rabbitmq,
      EventQueuePublisher.live,
      EventQueueConsumer.live,
      Queues.rabbitClient
    )

  override val bootstrap: ZLayer[Any, Nothing, Unit] =
    Runtime.removeDefaultLoggers >>> SLF4J.slf4j

end Application
