package com.hadiubaidillah.user.models

import com.hadiubaidillah.shared.model.UserDTO
import com.hadiubaidillah.user.entity.UserAttribute
import com.hadiubaidillah.user.entity.UserEntity


fun UserDTO.Companion.from(userEntity: UserEntity, userAttribute: UserAttribute): UserDTO {
    return UserDTO(
        id = userAttribute.id,
        email = userEntity.email,
        emailVerified = userEntity.emailVerified,
        enable = userEntity.enable,
        name = userEntity.name,
        createdTimestamp = userEntity.createdTimestamp,
        taskLimit = userAttribute.taskLimit,
        urlLimit = userAttribute.urlLimit,
        locationQueryUsage = userAttribute.locationQueryUsage,
        locationQueryLimit = userAttribute.locationQueryLimit,
        locationQuerySubscription = userAttribute.locationQuerySubscription
    )
}