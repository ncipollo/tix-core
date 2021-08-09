package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Comments(
    val comments: List<Comment> = emptyList()
)
