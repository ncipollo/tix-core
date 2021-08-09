package org.tix.integrations.jira.status

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val self: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val name: String = "",
    val id: String = "",
    val statusCategory: StatusCategory = StatusCategory()
)
