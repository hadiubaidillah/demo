package com.hadiubaidillah.todo.route

import com.hadiubaidillah.shared.extractAccessTokenInfo
import com.hadiubaidillah.shared.plugins.makeAuthenticatedRequest
import com.hadiubaidillah.shared.plugins.parseAuthorizationToken
import com.hadiubaidillah.todo.entity.Task
import com.hadiubaidillah.todo.service.TaskService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.time.ZoneId
import java.util.UUID

fun Application.registerTaskRoutes() {
    val taskService: TaskService by inject()
    routing {
        route("/hello") {
            get {
                call.respond("TODO API!")
            }
        }
        authenticate("auth-jwt") {
            route("/") {
                get {
                    call.respond(taskService.getAllByAuthorId(UUID.fromString(extractAccessTokenInfo(call)!!.id)))
                }
                get("limit"){
                    call.respond(makeAuthenticatedRequest("https://dev.hadiubaidillah.my.id/user/api/v1/user/attribute/me/task/limit", call.request.parseAuthorizationToken()!!).toInt())
                }
                get("{id}") {
                    val id = call.parameters["id"]
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@get
                    }
                    val task = taskService.getById(UUID.fromString(id))
                    if (task == null) {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    } else {
                        call.respond(task)
                    }
                }
                post {
                    val task = call.receive<Task>()
                    val timeZone = ZoneId.of(call.request.headers["Time-Zone"] ?: System.getenv("DEFAULT_TIME_ZONE"))
                    val accessTokenInfo = extractAccessTokenInfo(call)
                        ?: throw IllegalStateException("Missing or malformed access token info")

                    task.authorId = accessTokenInfo.id
                    call.respond(
                        HttpStatusCode.Created,
                        taskService.add(task, timeZone, accessTokenInfo)
                    )
                }
                put("{id}") {
                    val id = call.parameters["id"]
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@put
                    }
                    val task = call.receive<Task>()
                    val timeZone = ZoneId.of(call.request.headers["Time-Zone"]?: System.getenv("DEFAULT_TIME_ZONE"))
                    val accessTokenInfo = extractAccessTokenInfo(call)
                        ?: throw IllegalStateException("Missing or malformed access token info")
                    task.authorId = accessTokenInfo.id
                    if (taskService.update(UUID.fromString(id), task, timeZone, accessTokenInfo)) {
                        call.respond(HttpStatusCode.OK)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }
                }
                delete("{id}") {
                    val id = call.parameters["id"]
                    val timeZone = ZoneId.of(call.request.headers["Time-Zone"]?: System.getenv("DEFAULT_TIME_ZONE"))
                    val accessTokenInfo = extractAccessTokenInfo(call)
                        ?: throw IllegalStateException("Missing or malformed access token info")
                    if (id == null) {
                        call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                        return@delete
                    }
                    if (taskService.delete(UUID.fromString(id), timeZone, accessTokenInfo)) {
                        call.respond(HttpStatusCode.NoContent)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Task not found")
                    }
                }
            }
        }
    }
}