package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Watcher(
    val self: String = "",
    val name: String = "",
    val displayName: String = "",
    val active: Boolean = false
)
