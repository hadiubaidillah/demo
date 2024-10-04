package com.hadiubaidillah.notification.entity

import com.hadiubaidillah.shared.model.Recipient
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.json

object Notifications : Table("tbl_notifications") {
    val id = uuid("id")
    val authorId = uuid("author_id")
    val code = varchar("code", 50) references Templates.code
    val read = bool("read").default(false)
    val sent = bool("sent").default(false)
    val to = json<List<Recipient>>("to", Json)
    val cc = json<List<Recipient>>("cc", Json)
    val bcc = json<List<Recipient>>("bcc", Json)
    val parameters = json<String>("parameters", Json)
    val createdAt = long("created_at")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Notification(
    var id: String? = null,
    var authorId: String? = null,
    val code: String,
    var read: Boolean,
    val sent: Boolean,
    val to: List<Recipient>,
    val cc: List<Recipient>,
    val bcc: List<Recipient>,
    val parameters: String? = null,
    var createdAt: Long? = null,
)