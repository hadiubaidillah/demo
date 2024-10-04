package com.hadiubaidillah.shared.plugins

import org.jetbrains.exposed.sql.Database

import io.ktor.server.application.Application
import java.sql.DriverManager

fun Application.configureDatabases(embedded: Boolean = false) {
    if (embedded) {
        DriverManager.getConnection("jdbc:h2:mem:${System.getenv("DATABASE_USER")};DB_CLOSE_DELAY=-1", System.getenv("DATABASE_USER"), System.getenv("DATABASE_PASSWORD"))
    } else {
        Database.connect(
            System.getenv("DATABASE_URL"),
            user = System.getenv("DATABASE_USER"),
            password = System.getenv("DATABASE_PASSWORD")
        )
    }
}