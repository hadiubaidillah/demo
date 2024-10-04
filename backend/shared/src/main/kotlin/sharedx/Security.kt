//package com.hadiubaidillah.shared
//
//import com.auth0.jwk.JwkProviderBuilder
//import com.hadiubaidillah.plugins.BASE_URL
//import com.hadiubaidillah.plugins.KEYCLOAK_AUDIENCE
//import com.hadiubaidillah.plugins.KEYCLOAK_ISSUER
//import com.hadiubaidillah.plugins.KEYCLOAK_OPEN_ID_CONNECT_URL
//import com.hadiubaidillah.plugins.KEYCLOAK_REALM
//import io.ktor.server.application.*
//import io.ktor.server.auth.*
//import io.ktor.server.response.*
//import io.ktor.http.*
//import io.ktor.server.auth.jwt.*
//import io.ktor.server.plugins.cors.routing.CORS
//import kotlinx.serialization.Serializable
//import java.net.URI
//import java.util.concurrent.TimeUnit
//
//val jwkProvider = JwkProviderBuilder(URI.create("${KEYCLOAK_OPEN_ID_CONNECT_URL}/certs").toURL())
//    .cached(10, 24, TimeUnit.HOURS) // Cache kunci publik selama 24 jam
//    .rateLimited(10, 1, TimeUnit.MINUTES) // Batasi jumlah permintaan per menit
//    .build()
//
//fun Application.configureSecurity() {
//
//    install(CORS) {
//        anyHost()
//        allowHeader(HttpHeaders.ContentType)
//        allowHeader(HttpHeaders.Authorization)
//    }
//
//    install(Authentication) {
//        jwt("auth-jwt") {
//            realm = KEYCLOAK_REALM
//            verifier(jwkProvider, KEYCLOAK_ISSUER){
//                acceptLeeway(3) // Izinkan sedikit kelonggaran waktu
//                withAudience(KEYCLOAK_AUDIENCE)
//            }
//            validate { credential ->
//                val email = credential.payload.getClaim("email").asString()
//                val webOrigin = credential.payload.getClaim("allowed-origins").asList(String::class.java)
////                println(credential.payload.claims)
////                println(credential.payload.getClaim("resource_access"))
////                println("BASE_URL: $BASE_URL")
////                println("allowed-origins: $webOrigin")
//
//                if (email.isNotEmpty() && BASE_URL in webOrigin) {
//                    JWTPrincipal(credential.payload)
//                } else {
//                    null
//                }
//            }
//            challenge { _, _ ->
//                call.respondText { "Access Denied!" }
//                //call.respondRedirect("/login")
//            }
//        }
//    }
//}
//
//@Serializable
//data class Session(val accessToken: String, val idToken: String? = null)