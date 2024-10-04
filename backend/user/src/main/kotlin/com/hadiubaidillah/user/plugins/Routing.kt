package com.hadiubaidillah.user.plugins

import com.hadiubaidillah.user.route.registerUserEntitiesRoutes
import com.hadiubaidillah.user.route.registerUserAttributesRoutes
import com.hadiubaidillah.user.route.registerUserRoutes
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
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
            registerUserAttributesRoutes()
        }
        get("/") {
            call.respond("USER Service!")
        }
    }

    registerUserEntitiesRoutes()
    registerUserRoutes()

}