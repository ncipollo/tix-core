package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.user.User

@Serializable
data class Comment(
    val id: String = "",
    val self: String = "",
    val name: String = "",
    val author: User = User(),
    val body: String = "",
    val updateAuthor: User,
    val updated: String = "",
    val created: String = "",
    val visibility: CommentVisibility = CommentVisibility()
)
