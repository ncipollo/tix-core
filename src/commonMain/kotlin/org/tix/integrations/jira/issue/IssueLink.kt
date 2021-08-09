package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class IssueLink(
    val id: String = "",
    val self: String = "",
    val type: IssueLinkType = IssueLinkType(),
    val outwardIssue: Issue? = null,
    val inwardIssue: Issue? = null,
)
