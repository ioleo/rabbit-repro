package com.example.repro.queue

import com.example.repro.config.RabbitMqConfig
import com.example.repro.model.Event
import com.comcast.ip4s.*
import lepus.client.*
import lepus.protocol.domains.*
import zio.*
import zio.json.*
import zio.stream.*

final case class EventQueueConsumerLive(
  stream: ZStream[Any, Throwable, DeliveredMessage[String]],
  config: RabbitMqConfig
) extends EventQueueConsumer:
  def start() =
    stream.tap { case DeliveredMessage(consumerTag, deliveryTag, redelivered, exchange, routingKey, message) =>
      message.payload.fromJson[Event] match
        case Left(err)    => ZIO.logInfo(s"[RabbitMq] Unable to decode event due to: $err")
        case Right(event) => ZIO.logInfo(s"[RabbitMq] Consumed ${event.action} [${event.timestamp.toEpochMilli}]")
    }.runDrain
