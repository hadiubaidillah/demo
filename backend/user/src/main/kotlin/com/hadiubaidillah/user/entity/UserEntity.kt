package com.hadiubaidillah.user.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object UserEntities : Table("USER_ENTITY") {
    val id = varchar("ID", 36) //references UserAttributes.id
    val email = varchar("EMAIL", 255)
    val emailConstraint = varchar("EMAIL_CONSTRAINT", 255)
    val emailVerified = bool("EMAIL_VERIFIED")
    val enable = bool("ENABLED")
    val federationLink = varchar("FEDERATION_LINK", 255)
    val firstName = varchar("FIRST_NAME", 255)
    val lastName = varchar("LAST_NAME", 255)
    val realmId = varchar("REALM_ID", 255)
    val username = varchar("USERNAME", 255)
    val createdTimestamp = long("CREATED_TIMESTAMP")
    val serviceAccountClientLink = varchar("SERVICE_ACCOUNT_CLIENT_LINK", 255)
    val notBefore = integer("NOT_BEFORE")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class UserEntity(
    var id: String? = null,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val enable: Boolean,
    val name: String? = null,
    val createdTimestamp: Long? = null,
)