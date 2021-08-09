package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.user.User

@Serializable
data class WorklogRecord(
    val self: String = "",
    val author: User? = null,
    val updateAuthor: User? = null,
    val comment: String = "",
    val created: Long? = null,
    val started: Long? = null,
    val timeSpent: String = "",
    val timeSpendSeconds: Int = 0,
    val id: String = "",
    val issueId: String = ""
)
