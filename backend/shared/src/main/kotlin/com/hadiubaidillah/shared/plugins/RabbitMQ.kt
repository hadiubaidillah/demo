package com.hadiubaidillah.shared.plugins

import com.rabbitmq.client.ConnectionFactory

 //import com.rabbitmq.client.Channel
//import com.rabbitmq.client.Connection
//import io.ktor.server.application.*

val factory = ConnectionFactory().apply {
    host = System.getenv("RABBITMQ_HOST")
    port = System.getenv("RABBITMQ_PORT").toInt()
    username = System.getenv("RABBITMQ_USERNAME")
    password = System.getenv("RABBITMQ_PASSWORD")
    virtualHost = System.getenv("RABBITMQ_VIRTUAL_HOST")
    isAutomaticRecoveryEnabled = true // Mengaktifkan auto-recovery jika koneksi terputus
}

//lateinit var rabbitChannel: Channel
//lateinit var rabbitConnection: Connection
//
//fun Application.configureRabbitMQProducer() {
//
//    // Create connection dan channel RabbitMQ
//    rabbitConnection = factory.newConnection()
//    rabbitChannel = rabbitConnection.createChannel()
//
//    initRabbitMQProducer()
//}
//
//fun initRabbitMQProducer() {
//
//    val exchangeName = System.getenv("RABBITMQ_EXCHANGE")
//    val queueName = System.getenv("RABBITMQ_TASKS_QUEUE")
//    val routingKey = System.getenv("RABBITMQ_TASKS_ROUTING_KEY")
//
//    // Declare Exchange
//    rabbitChannel.exchangeDeclare(exchangeName, "direct", true)
//
//    // Declare Quorum Queue
//    rabbitChannel.queueDeclare(queueName, true, false, false, mapOf("x-queue-type" to "quorum"))
//
//    // Bind Queue ke Exchange with Routing Key
//    rabbitChannel.queueBind(queueName, exchangeName, routingKey)
//
//}