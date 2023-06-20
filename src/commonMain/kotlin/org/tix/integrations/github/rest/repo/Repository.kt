package org.tix.integrations.github.rest.repo

import kotlinx.serialization.Serializable
import org.tix.integrations.github.rest.user.User

@Serializable
data class Repository(
    val id: Long = 0,
    val nodeId: String = "",
    val name: String = "",
    val fullName: String = "",
    val owner: User = User(),
    val private: Boolean = false,
    val forked: Boolean = false,
    val url: String = "",
    val htmlUrl: String = "",
)
