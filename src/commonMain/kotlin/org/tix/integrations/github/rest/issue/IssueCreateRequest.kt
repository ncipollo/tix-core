package org.tix.integrations.github.rest.issue

import kotlinx.serialization.Serializable

@Serializable
data class IssueCreateRequest(
    val title: String = "",
    val body: String = "",
    val assignees: List<String> = emptyList(),
    val milestone: Long? = null,
    val labels: List<String> = emptyList()
)