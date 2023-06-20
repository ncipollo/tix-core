package org.tix.integrations.github.rest.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.github.state.State
import org.tix.integrations.github.state.StateReason

@Serializable
data class IssueUpdateRequest(
    val title: String? = null,
    val body: String? = null,
    val assignees: List<String> = emptyList(),
    val milestone: Long? = null,
    val labels: List<String> = emptyList(),
    val state: State? = null,
    val stateReason: StateReason? = null
)