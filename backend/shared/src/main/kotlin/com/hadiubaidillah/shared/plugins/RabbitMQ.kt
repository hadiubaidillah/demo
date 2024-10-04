package com.hadiubaidillah.shared.plugins

import com.rabbitmq.client.ConnectionFactory

val factory = ConnectionFactory().apply {
    host = System.getenv("RABBITMQ_HOST")
    port = System.getenv("RABBITMQ_PORT").toInt()
    username = System.getenv("RABBITMQ_USERNAME")
    password = System.getenv("RABBITMQ_PASSWORD")
    virtualHost = System.getenv("RABBITMQ_VIRTUAL_HOST")
    isAutomaticRecoveryEnabled = true // Mengaktifkan auto-recovery jika koneksi terputus
}