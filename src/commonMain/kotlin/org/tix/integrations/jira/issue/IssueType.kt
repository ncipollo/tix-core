package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class IssueType(
    val self: String = "",
    val id: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val name: String = "",
    val subtask: Boolean = false,
    val avatarId: Int = 0
)
