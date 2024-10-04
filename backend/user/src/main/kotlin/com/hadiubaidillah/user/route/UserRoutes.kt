package com.hadiubaidillah.user.route

import com.hadiubaidillah.shared.extractAccessTokenInfo
import com.hadiubaidillah.user.services.UserService
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.registerUserRoutes() {
    val userService: UserService by inject()
    routing {
        authenticate("auth-jwt") {
            route("/api/v1") {
                route("/user") {
                    get("me") {
                        val id = extractAccessTokenInfo(call)!!.id
                        call.respond(userService.getUserEntityAndAttribute(id))
                    }
                }
            }
        }
    }
}