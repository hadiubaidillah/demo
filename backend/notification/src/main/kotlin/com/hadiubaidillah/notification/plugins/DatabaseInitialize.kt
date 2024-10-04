package com.hadiubaidillah.notification.plugins

import com.hadiubaidillah.notification.entity.Notifications
import com.hadiubaidillah.notification.entity.Template
import com.hadiubaidillah.notification.entity.Templates
import com.hadiubaidillah.notification.repository.TemplateRepository
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.ktor.ext.inject

fun Application.configureDatabaseInitialize() {

    val templateRepository: TemplateRepository by inject()

    transaction{
        SchemaUtils.create(Notifications)
        SchemaUtils.create(Templates)

        val listAllTemplate: List<Template> = templateRepository.getAll()
        val existingCodes = listAllTemplate.map { it.code }.toSet()

        val templatesToInsert = mutableListOf(
            Template("tasks_created", "tasks_created.html", "Task Created - {{name}}"),
            Template("tasks_deleted", "tasks_deleted.html", "Task Deleted - {{name}}"),
            Template("tasks_finished", "tasks_finished.html", "Task Finished - {{name}}"),
            Template("tasks_completed", "tasks_completed.html", "Task Completed - {{name}}")
        )

        templatesToInsert.forEach { template ->
            if (template.code !in existingCodes) {
                Templates.insert {
                    it[code] = template.code
                    it[file] = template.file
                    it[subject] = template.subject
                }
            }
        }
    }

}
