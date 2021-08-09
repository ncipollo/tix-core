package org.tix.integrations.jira.transition

import kotlinx.serialization.Serializable
import org.tix.integrations.jira.status.Status

@Serializable
data class Transition(
    val id: String = "",
    val name: String = "",
    val to: Status = Status(),
    val fields: Map<String, TransitionField>
)
