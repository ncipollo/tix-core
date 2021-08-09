package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.user.User

@Serializable
data class Attachment(
    val self: String = "",
    val id: String = "",
    val filename: String = "",
    val author: User? = null,
    val created: String = "",
    val size: Int = 0,
    val mimeType: String = "",
    val content: String = "",
    val thumbnail: String = ""
)
