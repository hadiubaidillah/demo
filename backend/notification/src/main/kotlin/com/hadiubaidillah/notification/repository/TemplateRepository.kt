package com.hadiubaidillah.notification.repository

import com.hadiubaidillah.notification.entity.Template
import com.hadiubaidillah.notification.entity.Templates
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class TemplateRepository {

    private fun mapRowToModel(it: ResultRow): Template {
        return Template(
            code = it[Templates.code],
            file = it[Templates.file],
            subject = it[Templates.subject]
        )
    }

    fun getAll() = Templates.selectAll().map { mapRowToModel(it) }

    fun getByCode(code: String): Template? = transaction {
        Templates.selectAll().where { Templates.code eq code }.map { mapRowToModel(it) }.singleOrNull()
    }
}