package com.hadiubaidillah.notification.plugins

import com.hadiubaidillah.notification.repository.NotificationRepository
import com.hadiubaidillah.notification.repository.TemplateRepository
import com.hadiubaidillah.notification.service.NotificationService
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

val mainModule = module {
    single { TemplateRepository() }
    single { NotificationRepository() }
    single { NotificationService(get(), get()) }
}

fun Application.configureKoin() {
    startKoin {
        modules(mainModule)
    }
}