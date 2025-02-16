package com.hadiubaidillah.notification.route

import com.hadiubaidillah.notification.entity.Notification
import com.hadiubaidillah.notification.service.NotificationService
import com.hadiubaidillah.shared.extractAccessTokenInfo
import com.hadiubaidillah.shared.getUserId
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject
import java.time.ZoneId
import java.util.UUID

fun Application.registerNotificationRoutes() {
    val notificationService: NotificationService by inject()
    routing {
        authenticate("auth-jwt") {
            route("/") {
                get {
                    call.respond(
                        notificationService.getAll(getUserId(call))
                    )
                }
                get("{id}") {
                    val id = call.parameters["id"]
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@get
                    }
                    val notification = notificationService.getByIdAndAuthorId(UUID.fromString(id), getUserId(call))
                    if (notification == null) {
                        call.respond(HttpStatusCode.NotFound, "Notification not found")
                    } else {
                        call.respond(notification)
                    }
                }
                get("unreads") {
                    val notification = notificationService.getAllUnread(getUserId(call))
                    if (notification == null) {
                        call.respond(HttpStatusCode.NotFound, "Notification not found")
                    } else {
                        call.respond(notification)
                    }
                }
//                post {
//                    val notification = call.receive<Notification>()
//                    val timeZone = ZoneId.of(call.request.headers["Time-Zone"] ?: System.getenv("DEFAULT_TIME_ZONE"))
//                    val accessTokenInfo = extractAccessTokenInfo(call)
//                        ?: throw IllegalStateException("Missing or malformed access token info")
//
//                    notification.authorId = accessTokenInfo.id
//                    call.respond(
//                        HttpStatusCode.Created,
//                        notificationService.add(notification, timeZone, accessTokenInfo)
//                    )
//                }
//                put("{id}") {
//                    val id = call.parameters["id"]
//                    if (id == null) {
//                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
//                        return@put
//                    }
//                    val notification = call.receive<Notification>()
//                    val timeZone = ZoneId.of(call.request.headers["Time-Zone"]?: System.getenv("DEFAULT_TIME_ZONE"))
//                    val accessTokenInfo = extractAccessTokenInfo(call)
//                        ?: throw IllegalStateException("Missing or malformed access token info")
//                    notification.authorId = accessTokenInfo.id
//                    if (notificationService.update(UUID.fromString(id), notification, timeZone, accessTokenInfo)) {
//                        call.respond(HttpStatusCode.OK)
//                    } else {
//                        call.respond(HttpStatusCode.NotFound, "Notification not found")
//                    }
//                }
                delete("{id}") {
                    val id = call.parameters["id"]
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@delete
                    }
                    if (notificationService.delete(UUID.fromString(id), getUserId(call))) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Notification not found")
                    }
                }
                /*
                @PutMapping("/unreads")
                @ResponseStatus(HttpStatus.NO_CONTENT)
                fun markAllAsRead() {
                    service.markAllAsRead()
                }

                @PutMapping("/unreads/{id}")
                @ResponseStatus(HttpStatus.NO_CONTENT)
                fun markAsRead(@PathVariable("id") uuid: UUID) {
                    service.markAsRead(uuid)
                }
                 */
                put("unreads") {
                    val notification = notificationService.markAllAsRead(getUserId(call))
                    call.respond(notification)
                }
                put("unreads/{id}") {
                    val id = call.parameters["id"]
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@put
                    }
                    println("UUID.fromString(id): ${UUID.fromString(id)}")
                    println("getUserId(call): ${getUserId(call)}")
                    val notification = notificationService.markAsRead(UUID.fromString(id), getUserId(call))
                    call.respond(notification)
                }
            }
        }
    }
}