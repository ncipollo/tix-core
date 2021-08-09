package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class CommentVisibility(
    val type: String = "",
    val value: String = ""
)
