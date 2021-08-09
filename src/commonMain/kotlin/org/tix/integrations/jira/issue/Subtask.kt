package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Subtask(
    val id: String = "",
    val key: String = "",
    val self: String = "",
    val fields: IssueFields = IssueFields()
)
