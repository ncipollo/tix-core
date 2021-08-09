package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Epic(
    val id: Int = 0,
    val key: String = "",
    val self: String = "",
    val name: String = "",
    val summary: String = "",
    val done: Boolean = false
)
