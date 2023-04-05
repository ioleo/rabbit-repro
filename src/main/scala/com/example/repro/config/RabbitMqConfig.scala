package com.example.repro.config

final case class RabbitMqConfig(
  host: String,
  port: Int,
  username: String,
  password: String,
  ssl: Boolean,
  exchanges: RabbitMqExchangesConfig,
  queues: RabbitMqQueuesConfig,
  routingKeys: RabbitMqRoutingKeysConfig
)

final case class RabbitMqExchangesConfig(events: String)
final case class RabbitMqQueuesConfig(events: String)
final case class RabbitMqRoutingKeysConfig(events: String)
