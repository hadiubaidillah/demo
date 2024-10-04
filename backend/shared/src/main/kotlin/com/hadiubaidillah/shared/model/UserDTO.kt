package com.hadiubaidillah.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class UserDTO (
    val id: String,
    val email: String? = null,
    val emailVerified: Boolean? = null,
    val enable: Boolean,
    val name: String? = null,
    val createdTimestamp: Long? = null,
    val taskLimit: Int? = null,
    val urlLimit: Int? = null,
    val locationQueryUsage: Long? = null,
    val locationQueryLimit: Long? = null,
    val locationQuerySubscription: SubscriptionType? = null,
)

enum class SubscriptionType(val value: Long) {
    BASIC(10),
    STANDARD(20),
    PREMIUM(30)
}