package org.tix.integrations.jira.project

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.avatar.AvatarUrls
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.user.User
import org.tix.integrations.jira.version.Version

@Serializable
data class Project(
    val expand: String = "",
    val self: String = "",
    val id: String = "",
    val key: String = "",
    val description: String = "",
    val lead: User = User(),
    val components: List<ProjectComponent> = emptyList(),
    val issueTypes: List<IssueType> = emptyList(),
    val url: String = "",
    val email: String = "",
    val assigneeType: String = "",
    val versions: List<Version> = emptyList(),
    val name: String = "",
    val roles: Map<String, String> = emptyMap(),
    val avatarUrls: AvatarUrls = AvatarUrls(),
    val projectCategory: ProjectCategory = ProjectCategory()
)
