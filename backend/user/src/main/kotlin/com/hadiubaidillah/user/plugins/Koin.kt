package com.hadiubaidillah.user.plugins

import com.hadiubaidillah.user.repository.UserAttributesRepository
import com.hadiubaidillah.user.repository.UserEntitiesRepository
import com.hadiubaidillah.user.services.UserService
import io.ktor.server.application.Application
import org.koin.core.context.startKoin
import org.koin.dsl.module

val mainModule = module {
    single { UserEntitiesRepository() }
    single { UserAttributesRepository() }
    single { UserService(get(),get()) }
}

fun Application.configureKoin() {
    startKoin {
        modules(mainModule)
    }
}