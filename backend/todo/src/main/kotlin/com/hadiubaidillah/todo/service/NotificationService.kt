package com.hadiubaidillah.todo.service;

import com.hadiubaidillah.shared.AccessTokenInfo
import com.hadiubaidillah.shared.model.EmailRequest
import com.hadiubaidillah.shared.model.Recipient
import com.hadiubaidillah.shared.plugins.convertEpochToReadableTime
import com.hadiubaidillah.todo.entity.Task
import com.hadiubaidillah.todo.plugins.rabbitChannel
import com.rabbitmq.client.AMQP
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Instant
import java.time.ZoneId

class NotificationService() {

    fun sendNotification(emailRequest: EmailRequest): EmailRequest {
        val exchange = System.getenv("RABBITMQ_EXCHANGE")
        val routingKey = System.getenv("RABBITMQ_TASKS_ROUTING_KEY")

        // Pastikan exchange dan routing key tidak null
        if (exchange.isNullOrEmpty() || routingKey.isNullOrEmpty()) {
            throw IllegalStateException("RABBITMQ_EXCHANGE or RABBITMQ_TASKS_ROUTING_KEY must be set")
        }

        rabbitChannel.basicPublish(
            exchange,
            routingKey,
            null,
            Json.encodeToString(emailRequest).toByteArray()
        )

        return emailRequest
    }

    private fun sendDelayedNotification(emailRequest: EmailRequest, delayMs: Long): EmailRequest {
        val messageProperties = AMQP.BasicProperties.Builder()
            .headers(mapOf("x-delay" to delayMs))
            .build()

        val exchange = System.getenv("RABBITMQ_EXCHANGE")
        val routingKey = System.getenv("RABBITMQ_TASKS_ROUTING_KEY")

        // Pastikan exchange dan routing key tidak null
        if (exchange.isNullOrEmpty() || routingKey.isNullOrEmpty()) {
            throw IllegalStateException("RABBITMQ_DELAYED_EXCHANGE or RABBITMQ_TASKS_ROUTING_KEY must be set")
        }

        rabbitChannel.basicPublish(
            exchange,
            routingKey,
            messageProperties,
            Json.encodeToString(emailRequest).toByteArray()
        )

        return emailRequest
    }


    fun create(task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Task {
        val delayMs = task.endsIn!! - System.currentTimeMillis()
        println("delayMs = ${delayMs}")
        sendDelayedNotification(
            EmailRequest(
                to = listOf(
                    Recipient(accessTokenInfo.email, accessTokenInfo.name)
                ),
                authorId = accessTokenInfo.id,
                code = "tasks_finished",
                parameters = mapOf(
                    "name" to task.name.toString(),
                    "description" to task.description.toString(),
                    "ends_in" to convertEpochToReadableTime(task.endsIn, zoneId)
                )
            ),
            delayMs
        )

        return sendNotification(EmailRequest(
            to = listOf(
                Recipient(accessTokenInfo.email, accessTokenInfo.name)
            ),
            authorId = accessTokenInfo.id,
            code = "tasks_created",
            parameters = mapOf(
                "name" to task.name.toString(),
                "description" to task.description.toString(),
                "ends_in" to convertEpochToReadableTime(task.endsIn, zoneId)
            )
        )).let { task }

    }

    fun update(task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Task {

        return sendNotification(EmailRequest(
            to = listOf(
                Recipient(accessTokenInfo.email, accessTokenInfo.name)
            ),
            authorId = accessTokenInfo.id,
            code = "tasks_updated",
            parameters = mapOf(
                "name" to task.name.toString(),
                "description" to task.description.toString(),
                "ends_in" to convertEpochToReadableTime(task.endsIn!!, zoneId)
            )
        )).let { task }

    }

    fun delete(task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Boolean {

        return sendNotification(EmailRequest(
            to = listOf(
                Recipient(accessTokenInfo.email, accessTokenInfo.name)
            ),
            authorId = accessTokenInfo.id,
            code = "tasks_deleted",
            parameters = mapOf(
                "name" to task.name.toString(),
                "description" to task.description.toString(),
                "ends_in" to convertEpochToReadableTime(task.endsIn!!, zoneId)
            )
        )).let { true }

    }

    fun finished(task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Task {

        return sendNotification(EmailRequest(
            to = listOf(
                Recipient(accessTokenInfo.email, accessTokenInfo.name)
            ),
            authorId = accessTokenInfo.id,
            code = "tasks_finished",
            parameters = mapOf(
                "name" to task.name.toString(),
                "description" to task.description.toString(),
                "ends_in" to convertEpochToReadableTime(task.endsIn!!, zoneId)
            )
        )).let { task }

    }

}