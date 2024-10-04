package com.hadiubaidillah.todo.entity

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table

object Tasks : Table("tbl_task") {
    val id = uuid("id")
    val name = varchar("name", 255)
    val description = text("description")
    val createdAt = long("created_at")
    val endsIn = long("ends_in")
    val completed = bool("completed").default(false)
    val authorId = uuid("author_id")

    override val primaryKey = PrimaryKey(id)
}

@Serializable
data class Task(
    var id: String? = null,
    val name: String? = null,
    val description: String? = null,
    var createdAt: Long? = null,
    val endsIn: Long? = null,
    val completed: Boolean = false,
    var authorId: String? = null,
)

@Serializable
data class TaskLimitData(
    val limit: Int,
    val tasks: List<Task>
)