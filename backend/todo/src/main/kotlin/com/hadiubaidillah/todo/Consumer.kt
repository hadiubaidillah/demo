package com.hadiubaidillah.todo

import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


fun main() {

    // Kirim pesan ke RabbitMQ
    val factory = ConnectionFactory().apply {
        host = "node0" // sesuaikan dengan konfigurasi RabbitMQ
        port = 5672
    }
    val connection = factory.newConnection()
    val channel = connection.createChannel()

    val exchangeName = "notification"
    val queueName = "email"
    val routingKey = "email"

    // Deklarasi Exchange dan Queue
    channel.exchangeDeclare(exchangeName, "direct", true)
    // Deklarasi Quorum Queue
    val args = HashMap<String, Any>()
    args["x-queue-type"] = "quorum" // Menentukan tipe queue sebagai quorum
    channel.queueDeclare(queueName, true, false, false, args)

    // Bind Queue ke Exchange dengan Routing Key
    channel.queueBind(queueName, exchangeName, routingKey)

    val deliverCallback = DeliverCallback { _, delivery ->
        val message = String(delivery.body)
        //println("[x] Received '$message'")
        // Parse JSON menjadi objek EmailRequest
        //val emailData = parseEmailRequest(message)

        // Kirim email menggunakan Jakarta Mail
        //sendEmail(emailData.to, emailData.subject, emailData.body)
    }

    println("Waiting for messages in queue: $queueName")
    channel.basicConsume(queueName, true, deliverCallback) { _ -> }


//    channel.close()
//    connection.close()
}
