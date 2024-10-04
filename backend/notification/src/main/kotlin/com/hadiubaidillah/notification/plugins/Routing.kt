package com.hadiubaidillah.notification.plugins

import com.hadiubaidillah.notification.route.registerNotificationRoutes
import com.hadiubaidillah.shared.model.EmailRequest
import com.rabbitmq.client.ConnectionFactory
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {

        authenticate("auth-jwt") {
            get("/account") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                call.respondText("Hello, $email!")
            }
        }

        registerNotificationRoutes()

        get("/version") {
            call.respond("Notification Service! version 1.0.0")
        }

        post("/send-email") {
            val emailRequest = call.receive<EmailRequest>()
//            try {
//                emailRequest.sendNoReplyEmail()
//                call.respond(HttpStatusCode.OK, "Email sent successfully")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                call.respond(HttpStatusCode.Conflict, "Failed to send email")
//            }
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
                        "to": "${emailRequest.to}",
                    }
                """.trimIndent()

            channel.basicPublish(exchangeName, routingKey, null, message.toByteArray())

            call.respondText("Email request sent to RabbitMQ")
            channel.close()
            connection.close()
        }



        post("/send-email-2") {
            val params = call.receiveParameters()
            println("Params: ${params["to"]}")
            val to = params.getAll("to")?.map { it.trim() } ?: return@post call.respondText(
                "Missing email destination parameter", status = HttpStatusCode.BadRequest
            )

            println("Params: $to")
//            val emailRequest: EmailRequest = EmailRequest(
//                to = TODO(),
//                cc = TODO(),
//                bcc = TODO(),
//                subject = TODO(),
//                body = TODO()
//            )
            call.respond(to)
        }

    }
}
