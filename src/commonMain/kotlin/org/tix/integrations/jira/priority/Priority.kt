package org.tix.integrations.jira.priority

import kotlinx.serialization.Serializable

@Serializable
data class Priority(
    val self: String = "",
    val iconUrl: String = "",
    val name: String = "",
    val id: String = "",
    val statusColor: String = "",
    val description: String = ""
)
