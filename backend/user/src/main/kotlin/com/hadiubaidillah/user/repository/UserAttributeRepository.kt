package com.hadiubaidillah.user.repository

import com.hadiubaidillah.user.entity.UserAttributes
import com.hadiubaidillah.user.entity.UserAttribute
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserAttributesRepository {

    private fun mapRowToDto(it: ResultRow): UserAttribute {
        return UserAttribute(
            id = it[UserAttributes.id].toString(),
            taskLimit = it[UserAttributes.taskLimit],
            urlLimit = it[UserAttributes.urlLimit],
            locationQueryUsage = it[UserAttributes.locationQueryUsage],
            locationQueryLimit = it[UserAttributes.locationQueryLimit],
            locationQuerySubscription = it[UserAttributes.locationQuerySubscription],
        )
    }

    fun getAll(): List<UserAttribute> = transaction {
        UserAttributes.selectAll().map { mapRowToDto(it) }
    }

    fun getById(id: String): UserAttribute? = transaction {
        UserAttributes.selectAll().where { UserAttributes.id eq id }.map { mapRowToDto(it) }.singleOrNull()
    }

    fun add(userAttribute: UserAttribute): UserAttribute = transaction {
        val returning = UserAttributes.insert {
            it[id] = userAttribute.id
        }
        return@transaction returning.resultedValues!!.map { mapRowToDto(it) }.first()
    }

    fun update(id: String, userAttributes: UserAttribute): Boolean = transaction {
        UserAttributes.update({ UserAttributes.id eq id }) {
            userAttributes.apply {
                taskLimit?.let { it1 -> it[UserAttributes.taskLimit] = it1 }
                urlLimit?.let { it1 -> it[UserAttributes.urlLimit] = it1 }
                locationQueryUsage?.let { it1 -> it[UserAttributes.locationQueryUsage] = it1 }
                locationQueryLimit?.let { it1 -> it[UserAttributes.locationQueryLimit] = it1 }
                locationQuerySubscription?.let { it1 -> it[UserAttributes.locationQuerySubscription] = it1 }
            }
        } > 0
    }

}
