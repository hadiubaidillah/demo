package com.hadiubaidillah.todo.plugins

import com.hadiubaidillah.shared.model.EmailRequest
import com.hadiubaidillah.todo.route.registerTaskRoutes
import com.hadiubaidillah.todo.service.NotificationService
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import kotlin.getValue

fun Application.configureRouting() {
    val notificationService: NotificationService by inject()
    routing {
        authenticate("auth-jwt") {
            get("/account") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                call.respondText("Hello, $email!")
            }
        }
        get("/version") {
            call.respond("TODO Service! version 1.0.0")
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
            notificationService.sendNotification(emailRequest)
            //notificationService.createTaskNotification()
            call.respondText("Email request sent to RabbitMQ")
        }
    }

    registerTaskRoutes()

}