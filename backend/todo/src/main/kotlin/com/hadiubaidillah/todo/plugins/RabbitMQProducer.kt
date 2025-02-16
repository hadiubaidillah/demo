package com.hadiubaidillah.todo.plugins

import com.hadiubaidillah.shared.plugins.factory
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import io.ktor.server.application.*

lateinit var rabbitChannel: Channel
lateinit var rabbitConnection: Connection

fun Application.configureRabbitMQProducer() {

    // Create connection dan channel RabbitMQ
    rabbitConnection = factory.newConnection()
    rabbitChannel = rabbitConnection.createChannel()

    initRabbitMQProducer()
}

fun initRabbitMQProducer() {

    println("Starting RabbitMQ")

    val exchangeName = System.getenv("RABBITMQ_EXCHANGE")
    val queueName = System.getenv("RABBITMQ_TASKS_QUEUE")
    val routingKey = System.getenv("RABBITMQ_TASKS_ROUTING_KEY")

    // Declare Exchange
    rabbitChannel.exchangeDeclare(exchangeName, "x-delayed-message", true, false, mapOf<String, Any>("x-delayed-type" to "direct"))

    // Declare Quorum Queue
    rabbitChannel.queueDeclare(queueName, true, false, false, mapOf("x-queue-type" to "quorum"))

    // Bind Queue ke Exchange with Routing Key
    rabbitChannel.queueBind(queueName, exchangeName, routingKey)

    println("End of lines RabbitMQ")

}

