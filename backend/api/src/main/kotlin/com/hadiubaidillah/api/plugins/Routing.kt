package com.hadiubaidillah.api.plugins

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.content.readAllParts
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.request.contentLength
import io.ktor.server.request.contentType
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.receiveParameters
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.toByteArray

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello Ini API!")
        }
        get("/hello") {
            call.respondText("Hello Hadi!")
        }
        get("/json-test") {
            call.respond(mapOf("message" to "Hello, World!"))
        }
        route("notification") {
            handleProxyRequest("http://localhost:82")
        }
        route("user") {
            handleProxyRequest("http://localhost:84")
        }
        route("pos") {
            handleProxyRequest("http://localhost:85")
        }
        route("task") {
            handleProxyRequest("http://localhost:86")
        }
        route("url") {
            handleProxyRequest("http://localhost:87")
        }
    }
}

private fun Routing.handleProxyRequest(targetUrl: String) {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    // Wildcard route untuk menangani semua path di bawah route yang diatur
    route("{...}") {
        handle {
            // Tangkap path dinamis
            println("call.request.uri: " + call.request.uri)
            val proxyRequestPath = removeFirstSegment(call.request.uri) //call.parameters.getAll("{...}")?.joinToString("/") ?: ""
            // Buat URL lengkap yang akan diteruskan
            val url = "$targetUrl$proxyRequestPath"
            println("Proxying request to $url")
            println("call.request.httpMethod: " + call.request.httpMethod)
            println("call.parameters: " + call.parameters + " - " + call.parameters.getAll("{...}")?.joinToString("/"))
            println("call.request.contentType().withoutParameters()): " + call.request.contentType().withoutParameters())
            println("call.request.contentLength(): " + call.request.contentLength())
            println("ContentType.Application.FormUrlEncoded: " + ContentType.Application.FormUrlEncoded)
            println("ContentType.MultiPart.FormData: " + ContentType.MultiPart.FormData)

            // Siapkan request builder
            val requestBody = when (val contentType = call.request.contentType().withoutParameters()) {
                ContentType.Application.FormUrlEncoded -> {
                    val formParameters = call.receiveParameters()
                    FormDataContent(formParameters)
                }
                ContentType.MultiPart.FormData -> {
                    val multipartData = call.receiveMultipart()
                    MultiPartFormDataContent(multipartData.readAllParts())
                }
                else -> {
                    call.receiveText()
                }
            }

            val request = HttpRequestBuilder().apply {
                method = call.request.httpMethod
                url(url)
                headers.appendAll(call.request.headers)
                setBody(requestBody)
            }

            val response: HttpResponse = client.request(request)
            call.respondBytes(response.bodyAsChannel().toByteArray(), response.contentType(), response.status)
        }
    }
}

fun removeFirstSegment(input: String): String {
    val segments = input.split("/")
    // Jika panjang segmen lebih dari 1, kembalikan hasil dengan segmen pertama dihapus
    return if (segments.size > 1) {
        segments.drop(2).joinToString("/", "/")
    } else {
        input
    }
}