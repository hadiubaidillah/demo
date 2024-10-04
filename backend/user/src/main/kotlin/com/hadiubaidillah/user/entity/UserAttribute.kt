package com.hadiubaidillah.user.entity

import com.hadiubaidillah.shared.model.SubscriptionType
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object UserAttributes : Table("USER_ATTRIBUTE") {
    val id = varchar("ID", 36)
    val taskLimit = integer("TASK_LIMIT").default(5)
    val urlLimit = integer("URL_LIMIT").default(5)
    val locationQueryUsage = long("LOCATION_QUERY_USAGE").default(0)
    val locationQueryLimit = long("LOCATION_QUERY_LIMIT").default(10)
    val locationQuerySubscription = enumerationByName("LOCATION_QUERY_SUBSCRIPTION", 8, SubscriptionType::class)
        .default(SubscriptionType.BASIC)

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class UserAttribute(
    var id: String,
    val taskLimit: Int? = null,
    val urlLimit: Int? = null,
    val locationQueryUsage: Long? = null,
    val locationQueryLimit: Long? = null,
    val locationQuerySubscription: SubscriptionType? = null,
)