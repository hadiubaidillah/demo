package com.hadiubaidillah.todo

import com.rabbitmq.client.ConnectionFactory
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun getEpochInUTC(): Long {
    // Get the current instant (which is already in UTC)
    val currentInstant: Instant = Clock.System.now()

    // Convert the Instant to UTC explicitly (this is just to ensure correctness)
    val utcTime = currentInstant.toLocalDateTime(TimeZone.UTC)

    // Convert the UTC time back to Instant and get epoch milliseconds
    return utcTime.toInstant(TimeZone.UTC).toEpochMilliseconds()
}

fun main() {

    // Kirim pesan ke RabbitMQ
    val factory = ConnectionFactory().apply {
        host = "ubuntu" // sesuaikan dengan konfigurasi RabbitMQ
        port = 5672
    }
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    val exchangeName = "notification"
    val routingKey = "email"
    channel.exchangeDeclare(exchangeName, "direct", true)

    // Pesan yang dikirim ke RabbitMQ dalam bentuk JSON
    val message = """
                    {
                        "to": "ubaycreative@gmail.com",
                        "subject": "subject 2",
                        "body": "body 2"
                    }
                """.trimIndent()

    channel.basicPublish(exchangeName, routingKey, null, message.toByteArray())

    println("Email request sent to RabbitMQ Exchange")

    channel.close()
    connection.close()
}
