package com.hadiubaidillah.www.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.hadiubaidillah.shared.plugins.Session
import com.hadiubaidillah.shared.plugins.jwkProvider
import com.hadiubaidillah.shared.plugins.BASE_URL
import com.hadiubaidillah.shared.plugins.DOMAIN_URL
import com.hadiubaidillah.shared.plugins.KEYCLOAK_ISSUER
import com.hadiubaidillah.shared.plugins.KEYCLOAK_OPEN_ID_CONNECT_URL
import com.hadiubaidillah.shared.plugins.KEYCLOAK_REGISTRATION_URL
import com.hadiubaidillah.shared.plugins.PROTOCOL_URL
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.auth.OAuthAccessTokenResponse
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.clear
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.thymeleaf.ThymeleafContent
import java.security.interfaces.RSAPublicKey

fun Application.configureRouting() {
    routing {

        authenticate("auth-oauth-keycloak") {

            get("/login") {
                call.respondRedirect("/callback")
            }

            get("/callback") {
                val principal: OAuthAccessTokenResponse.OAuth2 = call.principal()!!
                call.sessions.set(Session(principal.accessToken, principal.extraParameters["id_token"] ))
                call.respondRedirect("/")
            }

        }

        // Access with Bearer ${accessToken}
        authenticate("auth-jwt") {
            get("/account") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal!!.payload.getClaim("email").asString()
                call.respondText("Hello, $email!")
            }
            post("/contact-submit"){
                // Your code to handle contact form submission goes here
                // For example, you can validate the form data, save it to a database, or send an email
                // Replace the following placeholder code with your actual implementation
                val name = call.request.queryParameters["name"] ?: ""
                val email = call.request.queryParameters["email"] ?: ""
                val message = call.request.queryParameters["message"] ?: ""

                // Validate the form data
                if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                    call.respondText("Form data is incomplete", status = HttpStatusCode.BadRequest)
                    return@post
                }

                // Save the form data to a database or perform any other necessary actions
                // Replace the following placeholder code with your actual implementation
                // For example, you can use an ORM library like Exposed or Hibernate to save the data to a database

                // Send an email
                // Replace the following placeholder code with your actual implementation
                // For example, you can use a library like Ktor Mail or JavaMail to send an email

                call.respondText("Form submitted successfully")
            }
        }

        get("/logout") {
            val session = call.sessions.get<Session>()
            if (session!= null) {
                val logoutUrl = "${KEYCLOAK_OPEN_ID_CONNECT_URL}/logout?post_logout_redirect_uri=${BASE_URL}/post-logout&id_token_hint=${session.idToken.orEmpty()}"
                call.respondRedirect(logoutUrl)
            } else {
                call.respondRedirect("/login")
            }
        }

        get("/post-logout") {
            call.sessions.clear<Session>()
            call.respondRedirect("/")
        }

        get("/register"){
            call.respondRedirect(KEYCLOAK_REGISTRATION_URL)
        }

        get("/") {
            // Ambil sesi atau cookie
            val accessToken = call.sessions.get<Session>()?.accessToken

            // Jika token tidak ada, tetap ke halaman index tanpa data user login
            val defaultModel =  mapOf(
                "protocolUrl" to PROTOCOL_URL,
                "domainUrl" to DOMAIN_URL,
                "login" to false
            )
            if (accessToken == null) {
                call.respond(ThymeleafContent("index", defaultModel))
                return@get
            }

            try {
                // Verify token menggunakan JWK dari Keycloak
                val decodedJWT: DecodedJWT = JWT.decode(accessToken)
                val jwk = jwkProvider.get(decodedJWT.keyId)
                val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)

                JWT.require(algorithm)
                    .withIssuer(KEYCLOAK_ISSUER)
                    .build()
                    .verify(accessToken)

                // Render halaman dengan Thymeleaf
                call.respond(ThymeleafContent("index", mapOf(
                    "protocolUrl" to PROTOCOL_URL,
                    "domainUrl" to DOMAIN_URL,
                    "login" to true,
                    "name" to decodedJWT.getClaim("name").asString(),
                    "email" to decodedJWT.getClaim("email").asString(),
                    "accessToken" to accessToken,
                )))
            }
            catch (e: TokenExpiredException) {
                e.printStackTrace()
                call.respondRedirect("/login")
            }
            catch (e: Exception) {
                // Jika token tidak valid, tetap ke halaman index tanpa data user login
                e.printStackTrace()
                call.respond(ThymeleafContent("index", defaultModel))
            }
        }

    }
}
