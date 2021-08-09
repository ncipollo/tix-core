package org.tix.integrations.jira.project

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.user.User

@Serializable
data class ProjectComponent(
    val self: String = "",
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val lead: User = User(),
    val assigneeType: String = "",
    val assignee: User = User(),
    val realAssigneeType: String = "",
    val realAssignee: User = User(),
    val isAssigneeTypeValid: Boolean = false,
    val project: String = "",
    val projectId: String = ""
)
