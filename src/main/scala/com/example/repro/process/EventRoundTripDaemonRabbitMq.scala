package com.example.repro.process

import java.time.Instant

import com.example.repro.model.Event
import com.example.repro.queue.{EventQueueConsumer, EventQueuePublisher}
import zio.*
import zio.stream.*

final case class EventRoundTripDaemonRabbitMq(publisher: EventQueuePublisher, consumer: EventQueueConsumer) extends EventRoundTripDaemon:
  def start(): ZIO[Any, Throwable, Unit] =
    for
      _     <- ZIO.logInfo("[RabbitMq] Starting infinite event round-trip")
      stream = ZStream.fromSchedule(Schedule.fixed(2.seconds)).map {
                 case l if l % 2 == 0 => Event(s"rabbitMq-$l", "subscribe", Instant.ofEpochSecond(l))
                 case l => Event(s"rabbitMq-$l", "unsubscribe", Instant.ofEpochSecond(l))
               }
      f1    <- consumer.start().fork
      f2    <- publisher.sendEvents(stream).fork
      _     <- Fiber.joinAll(Seq(f1, f2))
    yield ()
