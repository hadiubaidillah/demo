package com.hadiubaidillah.user.services

import com.hadiubaidillah.shared.model.UserDTO
import com.hadiubaidillah.user.entity.UserAttribute
import com.hadiubaidillah.user.entity.UserEntity
import com.hadiubaidillah.user.models.from
import com.hadiubaidillah.user.repository.UserAttributesRepository
import com.hadiubaidillah.user.repository.UserEntitiesRepository

class UserService(
    private val userEntitiesRepository: UserEntitiesRepository,
    private val userAttributesRepository: UserAttributesRepository
) {

    fun getEntityAll(): List<UserEntity> = userEntitiesRepository.getAll()

    fun getEntityById(id: String): UserEntity? = userEntitiesRepository.getById(id)

    fun getAttributeAll(): List<UserAttribute> = userAttributesRepository.getAll()

    fun getAttributeById(id: String): UserAttribute =
        userAttributesRepository.getById(id) ?: userEntitiesRepository.getById(id)?.let { userEntities ->
            userAttributesRepository.add(UserAttribute(userEntities.id.toString()))
        } ?: throw IllegalArgumentException("User not found")

    fun getUserEntityAndAttribute(id: String): UserDTO {
        val userEntity = getEntityById(id) ?: throw IllegalArgumentException("User not found")
        val userAttribute = getAttributeById(id)
        return UserDTO.from(userEntity, userAttribute)
    }
}