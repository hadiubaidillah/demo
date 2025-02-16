package com.hadiubaidillah.notification.repository

import com.hadiubaidillah.notification.entity.Notification
import com.hadiubaidillah.notification.entity.Notifications
import com.hadiubaidillah.shared.plugins.UUIDv7
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class NotificationRepository {

    private fun mapRowToModel(it: ResultRow): Notification {
        return Notification(
            id = it[Notifications.id].toString(),
            authorId = it[Notifications.authorId].toString(),
            code = it[Notifications.code],
            read = it[Notifications.read],
            sent = it[Notifications.sent],
            to = it[Notifications.to],
            cc = it[Notifications.cc],
            bcc = it[Notifications.bcc],
            parameters = it[Notifications.parameters],
            createdAt = it[Notifications.createdAt]
        )
    }

    fun findAllByAuthorId(id: UUID): List<Notification> = transaction {
        Notifications.selectAll().where { Notifications.authorId eq id }.orderBy( Notifications.createdAt, SortOrder.DESC ).map { mapRowToModel(it) }
    }

    fun findAllUnreadByAuthorId(authorId: UUID): Notification? = transaction {
        Notifications.selectAll().where {
            Notifications.read eq false
            Notifications.authorId eq authorId
        }
        .orderBy( Notifications.createdAt, SortOrder.DESC )
        .map { mapRowToModel(it) }.singleOrNull()
    }

    fun findByIdAndAuthorId(id: UUID, authorId: UUID): Notification? = transaction {
        println("findByIdAndAuthorId xxx")
        println("authorId: $authorId")
        Notifications.selectAll().where {
            (Notifications.id eq id) and
            (Notifications.authorId eq authorId)
        }.map { mapRowToModel(it) }.singleOrNull()
    }

    fun add(notification: Notification): Notification = transaction {
        val newId = UUIDv7.generate()
        val currentTime = System.currentTimeMillis()
        notification.id = id.toString()
        notification.createdAt = currentTime
        Notifications.insert {
            it[id] = newId
            it[authorId] = UUID.fromString(notification.authorId.toString())
            it[code] = notification.code
            it[read] = notification.read
            it[sent] = notification.sent
            it[to] = notification.to
            it[cc] = notification.cc
            it[bcc] = notification.bcc
            it[parameters] = notification.parameters.orEmpty()
            it[createdAt] = currentTime
        }.let { notification }
    }


    fun update(id: UUID, notification: Notification): Boolean = transaction {
        Notifications.update({ Notifications.id eq id }) { it ->
            it[authorId] = UUID.fromString(notification.authorId)
            it[code] = notification.code
            it[read] = notification.read
            it[sent] = notification.sent
            it[to] = notification.to
            it[cc] = notification.cc
            it[bcc] = notification.bcc
            it[parameters] = notification.parameters.orEmpty()
            it[createdAt] = notification.createdAt ?: System.currentTimeMillis()
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Notifications.deleteWhere { Notifications.id eq id } > 0
    }

    fun markAllAsRead(authorId: UUID): Boolean = transaction {
        Notifications.update({ Notifications.authorId eq authorId }) { it[read] = true } > 0
    }

    fun markAsRead(id: UUID, authorId: UUID): Boolean = transaction {
        Notifications.update({
            Notifications.id eq id
            Notifications.authorId eq authorId
        }) { it[read] = true } > 0
    }
}