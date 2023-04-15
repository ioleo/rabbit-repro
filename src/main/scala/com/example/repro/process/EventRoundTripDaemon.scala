package com.example.repro.process

import com.example.repro.queue.{EventQueueConsumer, EventQueuePublisher}
import zio.*

trait EventRoundTripDaemon:
  def start(): ZIO[Any, Throwable, Unit]

object EventRoundTripDaemon:

  val rabbitmq: URLayer[EventQueuePublisher & EventQueueConsumer, EventRoundTripDaemonRabbitMq] = ZLayer(
    for
      _         <- ZIO.logInfo("Building EventRoundTripDaemonRabbitMq...")
      publisher <- ZIO.service[EventQueuePublisher]
      consumer  <- ZIO.service[EventQueueConsumer]
      _         <- ZIO.logInfo("Built EventRoundTripDaemonRabbitMq")
    yield EventRoundTripDaemonRabbitMq(publisher, consumer)
  )
