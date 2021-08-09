package org.tix.integrations.jira.status

import kotlinx.serialization.Serializable

@Serializable
data class StatusCategory(
    val self: String = "",
    val id: Long = 0,
    val name: String = "",
    val key: String = "",
    val colorName: String = "",
)
