package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Watches(
    val self: String = "",
    val watchCount: Int = 0,
    val isWatching: Boolean = false,
    val watchers: List<Watcher> = emptyList()
)
