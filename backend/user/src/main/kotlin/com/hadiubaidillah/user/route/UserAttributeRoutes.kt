package com.hadiubaidillah.user.route

import com.hadiubaidillah.shared.extractAccessTokenInfo
import com.hadiubaidillah.user.services.UserService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerUserAttributesRoutes() {
    val userService: UserService by inject()
    routing {
        authenticate("auth-jwt") {
            route("/api/v1") {
                route("/user/attribute") {
                    get {
                        call.respond(userService.getAttributeAll())
                    }
                    get("me") {
                        call.respond(userService.getAttributeById(extractAccessTokenInfo(call)!!.id))
                    }
                    get("me/task/limit") {
                        call.respond(userService.getAttributeById(extractAccessTokenInfo(call)!!.id).taskLimit?: 0)
                    }
                    get("{id}") {
                        val id = call.parameters["id"]
                        if (id == null) {
                            call.respond(HttpStatusCode.BadRequest, "Missing or malformed id")
                            return@get
                        }
                        var userAttributes = userService.getAttributeById(id)
                        call.respond(userAttributes)
                    }
                }
            }
        }
    }
}