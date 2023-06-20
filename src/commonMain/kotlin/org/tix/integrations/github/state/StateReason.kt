package org.tix.integrations.github.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class StateReason {
    @SerialName("completed")
    COMPLETED,
    @SerialName("not_planned")
    NOT_PLANNED,
    @SerialName("reopened")
    REOPENED
}