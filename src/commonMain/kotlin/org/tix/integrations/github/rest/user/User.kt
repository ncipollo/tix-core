package org.tix.integrations.github.rest.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String? = null,
    val email: String? = null,
    val login: String = "",
    val id: Long = 0,
    val nodeId: String = "",
    val avatarUrl: String = "",
    val gravatarId: String? = null,
)
