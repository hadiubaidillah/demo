package com.hadiubaidillah.notification.entity

import com.hadiubaidillah.shared.model.Recipient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json

//id read sent type task_id

object Templates : Table("tbl_templates") {
    val code = varchar("code", 50)
    val file = varchar("file", 20)
    val subject = varchar("subject", 255)

    override val primaryKey = PrimaryKey(code)
}

@Serializable
data class Template(
    var code: String,
    var file: String,
    var subject: String
)