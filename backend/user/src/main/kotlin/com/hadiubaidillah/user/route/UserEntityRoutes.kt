package com.hadiubaidillah.user.route

import com.hadiubaidillah.shared.extractAccessTokenInfo
import com.hadiubaidillah.user.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerUserEntitiesRoutes() {
    val userService: UserService by inject()
    routing {
        authenticate("auth-jwt") {
            route("/api/v1") {
                route("/user/entity") {
//                    get {
//                        call.respond(userService.getEntityAll())
//                    }
                    get("me") {
                        call.respond(
                            userService.getEntityById(extractAccessTokenInfo(call)!!.id) ?:
                            throw IllegalArgumentException("User not found")
                        )
                    }
                    get("{id}") {
                        val id = call.parameters["id"]
                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                            return@get
                        }
                        val user = userService.getEntityById(id)
                        if (user == null) {
                            call.respond(HttpStatusCode.NotFound, "User Entity not found")
                        } else {
                            call.respond(user)
                        }
                    }
                }
            }
        }
    }
}