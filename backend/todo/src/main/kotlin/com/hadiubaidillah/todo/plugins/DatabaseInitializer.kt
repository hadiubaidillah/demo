package com.hadiubaidillah.todo.plugins

import com.hadiubaidillah.todo.entity.Tasks
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

import io.ktor.server.application.Application

fun Application.configureDatabaseInitialize() {
    Database.connect(
        System.getenv("DATABASE_URL"),
        user = System.getenv("DATABASE_USER"),
        password = System.getenv("DATABASE_PASSWORD")
    )
    transaction {
        SchemaUtils.create(Tasks)
    }
}