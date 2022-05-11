package com.baseio.kmm.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class FcmToken(
    val id: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: String? = null,
    val pushToken: String? = null,
    val profilePic: String? = null,
    val modifiedTime: String? = null,
    val platform: String? = null
)