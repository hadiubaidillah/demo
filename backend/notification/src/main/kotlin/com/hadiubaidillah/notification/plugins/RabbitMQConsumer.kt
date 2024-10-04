package com.hadiubaidillah.notification.plugins

import com.hadiubaidillah.notification.service.NotificationService
import com.hadiubaidillah.shared.model.EmailRequest
import com.hadiubaidillah.shared.plugins.factory
import com.rabbitmq.client.DeliverCallback
import io.ktor.server.application.Application
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import kotlin.getValue


@OptIn(DelicateCoroutinesApi::class)
fun Application.launchRabbitMQConsumer() {
    val notificationService: NotificationService by inject()
    GlobalScope.launch(Dispatchers.IO) { // suitable for long-term tasks that do not depend on any life cycle
        val connection = factory.newConnection()
        val channel = connection.createChannel()

        val queueName = System.getenv("RABBITMQ_TASKS_QUEUE")

        val deliverCallback = DeliverCallback { _, delivery ->
            val message = String(delivery.body)
            val emailRequest: EmailRequest = Json.decodeFromString<EmailRequest>(message)
            println("[x] Received: '$message'")

            notificationService.sendNoReplyEmail(emailRequest)
        }

        println("Waiting for messages in quorum queue: $queueName")
        channel.basicConsume(queueName, true, deliverCallback) { _ -> }
    }
}
