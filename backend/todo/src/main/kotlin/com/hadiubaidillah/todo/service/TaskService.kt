package com.hadiubaidillah.todo.service

import com.hadiubaidillah.shared.AccessTokenInfo
import com.hadiubaidillah.todo.entity.Task
import com.hadiubaidillah.todo.repository.TaskRepository
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.ZoneId
import java.util.UUID

class TaskService(
    private val taskRepository: TaskRepository,
    private val notificationService: NotificationService
) {

    fun getAllByAuthorId(authorId: UUID): List<Task> = taskRepository.getAllByAuthorId(authorId)

    fun getById(id: UUID): Task? = taskRepository.getById(id)

    fun add(task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Task =
        notificationService.create(taskRepository.add(task), zoneId, accessTokenInfo)


    fun update(id: UUID, task: Task, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Boolean =
        taskRepository.update(id, notificationService.update(task, zoneId, accessTokenInfo))

    fun delete(id: UUID, zoneId: ZoneId, accessTokenInfo: AccessTokenInfo): Boolean = transaction {
        val task: Task = getById(id) ?: throw IllegalArgumentException("Task not found")
        taskRepository.delete(UUID.fromString(task.id))
        notificationService.delete(task, zoneId, accessTokenInfo)
    }

}
