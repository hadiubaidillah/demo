package com.hadiubaidillah.todo.repository

import com.hadiubaidillah.shared.plugins.UUIDv7
import com.hadiubaidillah.todo.entity.Tasks
import com.hadiubaidillah.todo.entity.Task
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import java.util.UUID

class TaskRepository {

    private fun mapRowToTask(it: ResultRow): Task {
        return Task(
            id = it[Tasks.id].toString(),
            name = it[Tasks.name],
            description = it[Tasks.description],
            createdAt = it[Tasks.createdAt],
            endsIn = it[Tasks.endsIn],
            completed = it[Tasks.completed],
            authorId = it[Tasks.authorId].toString()
        )
    }

    fun getAll(): List<Task> = transaction {
        Tasks.selectAll().map { mapRowToTask(it) }
    }

    fun getAllByAuthorId(authorId: UUID): List<Task> = transaction {
        Tasks.selectAll().where { Tasks.authorId eq authorId }.map { mapRowToTask(it) }
    }

    fun getById(id: UUID): Task? = transaction {
        Tasks.selectAll().where { Tasks.id eq id }.map { mapRowToTask(it) }.singleOrNull()
    }

    fun add(task: Task): Task = transaction {
        val newId = UUIDv7.generate()
        val currentTime = System.currentTimeMillis()
        task.id = newId.toString()
        task.createdAt = currentTime
        Tasks.insert {
            it[id] = newId
            it[name] = task.name.orEmpty()
            it[description] = task.description.orEmpty()
            it[createdAt] = currentTime
            it[endsIn] = task.endsIn ?: currentTime
            it[authorId] = UUID.fromString(task.authorId.toString())
        }.let{ task }
    }


    fun update(id: UUID, task: Task): Boolean = transaction {
        Tasks.update({ Tasks.id eq id }) {
            task.name?.let { it1 -> it[name] = it1 }
            task.description?.let { it1 -> it[description] = it1 }
            task.endsIn?.let { it1 -> it[endsIn] = it1 }
            task.completed.let { it1 -> it[completed] = it1 }
            task.authorId?.let { it1 -> it[authorId] = UUID.fromString(it1) }
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Tasks.deleteWhere { Tasks.id eq id } > 0
    }
}
