package com.example.repro.queue

import com.example.repro.config.RabbitMqConfig
import com.example.repro.model.Event
import com.comcast.ip4s.*
import lepus.client.*
import lepus.protocol.domains.*
import zio.*
import zio.interop.catz.*
import zio.stream.*

trait EventQueuePublisher:

  def sendEvents(stream: ZStream[Any, Nothing, Event]): ZIO[Any, Throwable, Unit]

end EventQueuePublisher

object EventQueuePublisher:

  val live: ZLayer[Connection[Task] & RabbitMqConfig, Throwable, EventQueuePublisher] = ZLayer.scoped(
    for
      _             <- ZIO.logInfo("Building EventQueuePublisher...")
      connection    <- ZIO.service[Connection[Task]]
      config        <- ZIO.service[RabbitMqConfig]
      channel       <- connection.channel.toScopedZIO
      eventsExchange = ExchangeName.from(config.exchanges.events).toOption.get
      eventsQueue    = QueueName.from(config.queues.events).toOption.get
      eventsKey      = ShortString.from(config.routingKeys.events).toOption.get
      _             <- channel.exchange.declare(eventsExchange, ExchangeType.Direct)
      _             <- channel.queue.declare(eventsQueue)
      _             <- channel.queue.bind(eventsQueue, eventsExchange, eventsKey)
      _             <- ZIO.logInfo("Built EventQueuePublisher")
    yield EventQueuePublisherLive(channel.messaging, config)
  )

end EventQueuePublisher
