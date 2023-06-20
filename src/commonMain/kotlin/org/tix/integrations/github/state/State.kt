package org.tix.integrations.github.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class State {
    @SerialName("open")
    OPEN,
    @SerialName("closed")
    CLOSED
}