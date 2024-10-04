package com.hadiubaidillah.shared.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.request.ApplicationRequest
import kotlinx.coroutines.*

// Extension function untuk parsing Bearer token dari Authorization header
fun ApplicationRequest.parseAuthorizationToken(): String? {
    val authorizationHeader = this.headers[HttpHeaders.Authorization] ?: return null
    // Memastikan header diawali dengan "Bearer "
    if (authorizationHeader.startsWith("Bearer ", ignoreCase = true)) {
        // Mendapatkan token setelah "Bearer "
        return authorizationHeader.removePrefix("Bearer ").trim()
    }
    return null
}

// Membuat instance HttpClient sekali untuk digunakan berkali-kali
val client = HttpClient(CIO) {
    // Konfigurasi client jika diperlukan, misalnya timeout
    engine {
        requestTimeout = 5000 // 5 detik timeout untuk setiap request
    }
}

suspend fun makeAuthenticatedRequest(url: String, bearerToken: String): String {
    return client.get(url) {
        headers {
            append(HttpHeaders.Authorization, "Bearer $bearerToken")
        }
    }.bodyAsText() // Mengembalikan respons body sebagai string
}

// Contoh penggunaan fungsi secara cepat berkali-kali
fun main() = runBlocking {
    val bearerToken = "your_bearer_token"
    val url = "https://api.example.com/endpoint"

    // Melakukan beberapa request paralel untuk kecepatan maksimum
    val requests = List(10) {  // Misalnya 10 request sekaligus
        async {
            makeAuthenticatedRequest(url, bearerToken)
        }
    }

    // Menunggu semua request selesai
    requests.awaitAll().forEach { response ->
        println("Response: $response")
    }

    // Jangan lupa menutup HttpClient ketika tidak digunakan lagi (misalnya saat aplikasi berhenti)
    client.close()
}
