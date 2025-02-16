package com.hadiubaidillah.notification.entity

import com.hadiubaidillah.shared.model.EmailRequest
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
    val parameters = json<List<Parameter>>("parameters", Json)
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
    val parameters: List<Parameter>,
    var createdAt: Long? = null
)

@Serializable
data class Parameter(val key: String, val value: String)

fun convertMapToList(map: Map<String, String>): List<Parameter> {
    return map.map { (key, value) ->
        // Assuming the key is the email and the value is the name
        Parameter(key, value)
    }
}

// Extension function to convert EmailRequest to Notification
fun EmailRequest.toNotification(): Notification {

    return Notification(
        id = null, // Default value for id
        authorId = this.authorId,
        code = this.code,
        read = false, // Default value for read
        sent = false, // Default value for sent
        to = this.to,
        cc = this.cc ?: emptyList(), // Nullable, will be null if not provided
        bcc = this.bcc ?: emptyList(), // Nullable, will be null if not provided
        //subject = this.subject, // Nullable, will be null if not provided
        parameters = convertMapToList(this.parameters!!)
    )
}