package com.example.repro.queue

import com.example.repro.config.RabbitMqConfig
import com.example.repro.model.Event
import com.comcast.ip4s.*
import lepus.client.*
import lepus.client.apis.*
import lepus.protocol.domains.*
import zio.*
import zio.interop.catz.*
import zio.json.*
import zio.stream.*

final case class EventQueuePublisherLive(channel: NormalMessagingChannel[Task], config: RabbitMqConfig) extends EventQueuePublisher:

  def sendEvents(stream: ZStream[Any, Nothing, Event]): ZIO[Any, Throwable, Unit] =
    stream.foreach { event =>
      val eventJson = event.toJson
      channel.publish[String](
        ExchangeName.from(config.exchanges.events).toOption.get,
        routingKey = ShortString.from(config.routingKeys.events).toOption.get,
        Message(eventJson)
      ) *> ZIO.logInfo(s"[RabbitMq] Published event ${event.action} [${event.timestamp.toEpochMilli}]")
    }

end EventQueuePublisherLive
