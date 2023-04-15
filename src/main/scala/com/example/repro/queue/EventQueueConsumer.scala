package com.example.repro.queue

import com.example.repro.config.RabbitMqConfig
import com.example.repro.model.Event
import com.comcast.ip4s.*
import lepus.client.*
import lepus.protocol.domains.*
import zio.*
import zio.interop.catz.*
import zio.stream.*
import zio.stream.interop.fs2z.*

trait EventQueueConsumer:

  def start(): ZIO[Any, Throwable, Unit]

end EventQueueConsumer

object EventQueueConsumer:

  val live: ZLayer[Connection[Task] & RabbitMqConfig, Throwable, EventQueueConsumer] = ZLayer.scoped(
    for
      _          <- ZIO.logInfo("Building EventQueueConsumer...")
      connection <- ZIO.service[Connection[Task]]
      config     <- ZIO.service[RabbitMqConfig]
      channel    <- connection.channel.toScopedZIO
      stream      = channel.messaging.consume[String](QueueName.from(config.queues.events).toOption.get, ConsumeMode.NackOnError).toZStream()
      _          <- ZIO.logInfo("Built EventQueueConsumer")
    yield EventQueueConsumerLive(stream, config)
  )

end EventQueueConsumer
