package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.transition.Transition

@Serializable
data class Issue(
    val expand: String = "",
    val id: String = "",
    val self: String = "",
    val key: String = "",
    val fields: IssueFields? = null,
    val transitions: List<Transition> = emptyList()
) {
    val keyOrId = key.ifBlank { id }
}