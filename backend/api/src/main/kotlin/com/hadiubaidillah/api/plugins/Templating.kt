package com.hadiubaidillah.api.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver((
            ClassLoaderTemplateResolver().apply {
                prefix = "templates/thymeleaf/"
            }).apply {
                suffix = ".html"
                characterEncoding = "utf-8"
            })
    }
    routing {
        staticResources("css", "templates/thymeleaf/css")
        staticResources("js", "templates/thymeleaf/js")
        staticResources("images", "templates/thymeleaf/images")
        staticResources("fonts", "templates/thymeleaf/fonts")
        staticResources("demo", "templates/thymeleaf/demo")
    }
}