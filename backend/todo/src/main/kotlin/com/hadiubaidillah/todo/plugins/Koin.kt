package com.hadiubaidillah.todo.plugins

import com.hadiubaidillah.todo.repository.TaskRepository
import com.hadiubaidillah.todo.service.NotificationService
import com.hadiubaidillah.todo.service.TaskService
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

val mainModule = module {
    single { TaskRepository() }
    single { TaskService(get(), get()) }
    single { NotificationService() }
}

fun Application.configureKoin() {
    startKoin {
        modules(mainModule)
    }
}