package com.hadiubaidillah.user.repository

import com.hadiubaidillah.user.entity.UserEntities
import com.hadiubaidillah.user.entity.UserEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserEntitiesRepository {

    private fun mapRowToDto(it: ResultRow): UserEntity {
        return UserEntity(
            id = it[UserEntities.id].toString(),
            email = it[UserEntities.email],
            emailVerified = it[UserEntities.emailVerified],
            enable = it[UserEntities.enable],
            name = "${it[UserEntities.firstName]} ${it[UserEntities.lastName]}",
            createdTimestamp = it[UserEntities.createdTimestamp]
        )
    }

    fun getAll(): List<UserEntity> = transaction {
        UserEntities.selectAll().map { mapRowToDto(it) }
    }

    fun getById(id: String): UserEntity? = transaction {
        UserEntities.selectAll().where { UserEntities.id eq id }.map { mapRowToDto(it) }.singleOrNull()
    }

}
