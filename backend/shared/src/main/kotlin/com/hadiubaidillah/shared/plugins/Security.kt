package com.hadiubaidillah.shared.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.OAuthServerSettings
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.oauth
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respond
import io.ktor.server.sessions.SameSite
import io.ktor.server.sessions.Sessions
import io.ktor.server.sessions.cookie
import io.ktor.server.sessions.maxAge
import io.ktor.server.sessions.sameSite
import kotlinx.serialization.Serializable
import java.net.URI
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days

val jwkProvider = JwkProviderBuilder(URI.create("${KEYCLOAK_OPEN_ID_CONNECT_URL}/certs").toURL())
    .cached(10, 24, TimeUnit.HOURS) // Cache kunci publik selama 24 jam
    .rateLimited(10, 1, TimeUnit.MINUTES) // Batasi jumlah permintaan per menit
    .build()

fun Application.configureSecurity() {

    install(Sessions) {
        cookie<Session>("SESSION") {
            cookie.path = "/"
            cookie.domain = ".${DOMAIN_URL}"
            cookie.maxAge = 1.days
            cookie.httpOnly = true     // Opsional, untuk keamanan, pilih false jika ingin diakses lewat javascript juga
            cookie.secure = true       // Opsional, gunakan jika Anda menggunakan HTTPS, pilih false jika Anda menggunakan HTTP
            cookie.sameSite = SameSite.Lax // Default
            /*
            SameSite.None: Use when you need to share cookies across domains but ensure to set Secure.
            SameSite.Lax: A good default for most use cases, allowing cookies in top-level navigation but restricting them from being sent with subresources in cross-site requests.
            SameSite.Strict: Use for maximum security, ensuring cookies are never sent in cross-site requests, which may limit some cross-site functionalities.
            */
        }
    }

    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }

    install(Authentication) {
        oauth("auth-oauth-keycloak") {
            client = HttpClient(CIO)
            urlProvider = { "${BASE_URL}/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "keycloak",
                    authorizeUrl = "${KEYCLOAK_OPEN_ID_CONNECT_URL}/auth",
                    accessTokenUrl = "${KEYCLOAK_OPEN_ID_CONNECT_URL}/token",
                    clientId = KEYCLOAK_CLIENT_ID,
                    clientSecret = KEYCLOAK_CLIENT_SECRET.orEmpty(),
                    requestMethod = HttpMethod.Post,
                    defaultScopes = listOf("openid")
                )
            }
        }
        jwt("auth-jwt") {
            realm = KEYCLOAK_REALM
            verifier(jwkProvider, KEYCLOAK_ISSUER){
                acceptLeeway(3) // Izinkan sedikit kelonggaran waktu
                withAudience(KEYCLOAK_AUDIENCE)
            }
            validate { credential ->
                val emailVerified: Boolean = credential.payload.getClaim("email_verified").asBoolean()
//                val webOrigin = credential.payload.getClaim("allowed-origins").asList(String::class.java)
//                println(credential.payload.claims)
//                println(credential.payload.getClaim("email").asString())
//                println(credential.payload.getClaim("resource_access"))
//                println(credential.payload.getClaim("email_verified").asBoolean())
//                println("BASE_URL: $BASE_URL")
//                println("allowed-origins: $webOrigin")
//                println("BASE_URL in webOrigin: ${BASE_URL in webOrigin}")
//                if (email.isNotEmpty() && BASE_URL in webOrigin) {
                if(emailVerified) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Access Denied!")
//                when (exception) {
//                    is TokenExpiredException -> call.respond(HttpStatusCode.Unauthorized, "Token expired. Please refresh.")
//                    else -> call.respond(HttpStatusCode.Unauthorized, "Access Denied!")
//                }
                //call.respondRedirect("/login")
            }
        }
    }
}

// Model data session
// if you want to store more data in session, you can add more properties to this class
// for example: val refreshToken: String? = null
@Serializable
data class Session(val accessToken: String, val idToken: String? = null)