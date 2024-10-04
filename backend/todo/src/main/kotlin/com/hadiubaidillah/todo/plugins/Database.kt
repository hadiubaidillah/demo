//package com.hadiubaidillah.todo.plugins
//
//import com.hadiubaidillah.todo.entity.Tasks
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.SchemaUtils
//import org.jetbrains.exposed.sql.transactions.transaction
//
//import io.ktor.server.application.Application
//import java.sql.DriverManager
//
//fun Application.configureDatabases(embedded: Boolean = false) {
//    if (embedded) {
//        DriverManager.getConnection("jdbc:h2:mem:${System.getenv("DATABASE_USER")};DB_CLOSE_DELAY=-1", System.getenv("DATABASE_USER"), System.getenv("DATABASE_PASSWORD"))
//    } else {
//        Database.connect(
//            System.getenv("DATABASE_URL"),
//            user = System.getenv("DATABASE_USER"),
//            password = System.getenv("DATABASE_PASSWORD")
//        )
//    }
//    transaction {
//        SchemaUtils.create(Tasks)
//    }
//}