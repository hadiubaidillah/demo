package com.hadiubaidillah.shared.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val to: List<Recipient>,
    val cc: List<Recipient>? = null,
    val bcc: List<Recipient>? = null,
    val authorId: String,
    val code: String,
    var subject: String? = null,
    val parameters: Map<String, String>? = null,
)

@Serializable
data class Recipient(val email: String, val name: String? = null)