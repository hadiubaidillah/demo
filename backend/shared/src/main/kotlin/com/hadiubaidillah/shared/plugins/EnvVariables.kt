package com.hadiubaidillah.shared.plugins

val PROTOCOL_URL: String = System.getenv("PROTOCOL_URL")
val DOMAIN_URL: String = System.getenv("DOMAIN_URL")
val SUB_DOMAIN_URL: String = System.getenv("SUB_DOMAIN_URL")
val GATEWAY_SUB_DOMAIN_URL: String = System.getenv("GATEWAY_SUB_DOMAIN_URL")
val BASE_URL: String = "${PROTOCOL_URL}://${SUB_DOMAIN_URL}.${DOMAIN_URL}"
val GATEWAY_URL: String = "${PROTOCOL_URL}://${GATEWAY_SUB_DOMAIN_URL}.${DOMAIN_URL}"

//val PORTAL_SUB_DOMAIN_URL: String = System.getenv("PORTAL_SUB_DOMAIN_URL")
//val PORTAL_URL: String = "${PROTOCOL_URL}://${PORTAL_SUB_DOMAIN_URL}.${DOMAIN_URL}"

//val KEYCLOAK_PROTOCOL_URL: String = System.getenv("PROTOCOL_URL")
//val KEYCLOAK_DOMAIN_URL: String = System.getenv("KEYCLOAK_DOMAIN_URL")
//val KEYCLOAK_SUB_DOMAIN_URL: String = System.getenv("KEYCLOAK_SUB_DOMAIN_URL")
val KEYCLOAK_URL: String = System.getenv("KEYCLOAK_URL") //"${KEYCLOAK_PROTOCOL_URL}://${KEYCLOAK_SUB_DOMAIN_URL}.${KEYCLOAK_DOMAIN_URL}"
val KEYCLOAK_REALM: String = System.getenv("KEYCLOAK_REALM")
val KEYCLOAK_CLIENT_ID: String = System.getenv("KEYCLOAK_CLIENT_ID")
val KEYCLOAK_CLIENT_SECRET: String? = System.getenv("KEYCLOAK_CLIENT_SECRET")?: null
val KEYCLOAK_AUDIENCE: String = System.getenv("KEYCLOAK_AUDIENCE")

val KEYCLOAK_ISSUER = "${KEYCLOAK_URL}/realms/${KEYCLOAK_REALM}"
val KEYCLOAK_OPEN_ID_CONNECT_URL = "${KEYCLOAK_ISSUER}/protocol/openid-connect"
val KEYCLOAK_REGISTRATION_URL = "${KEYCLOAK_OPEN_ID_CONNECT_URL}/registrations?client_id=${KEYCLOAK_CLIENT_ID}&response_type=code&scope=openid&redirect_uri=${BASE_URL}/login"