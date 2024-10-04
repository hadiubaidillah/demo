package com.hadiubaidillah.shared

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import java.util.UUID

fun extractAccessTokenInfo(call: ApplicationCall): AccessTokenInfo? {
    val principal = call.principal<JWTPrincipal>()

    val authorId = principal?.getClaim("sub", String::class)
    val email = principal?.getClaim("email", String::class)
    val firstName = principal?.getClaim("given_name", String::class)
    val lastName = principal?.getClaim("family_name", String::class)
    val username =  principal?.getClaim("preferred_username", String::class)
    val verified = principal?.getClaim("email_verified", Boolean::class) == true

    return if (authorId != null && email != null && firstName != null && lastName != null && username != null) {
        AccessTokenInfo(
            id = authorId,
            email = email,
            firstName = firstName,
            lastName = lastName,
            username = username,
            verified = verified
        )
    } else {
        null
    }
}

fun getUserId(call: ApplicationCall) = UUID.fromString(extractAccessTokenInfo(call)!!.id)

data class AccessTokenInfo(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val verified: Boolean,
    val name: String = "$firstName $lastName"
)

fun printAllEnvironmentVariables() {
    System.getenv().forEach { (k, v) -> println("$k => $v") }
}