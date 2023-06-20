package org.tix.integrations.github.rest.milestone

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.tix.integrations.github.state.State

@Serializable
data class Milestone(
    val id: Long = 0,
    val nodeId: String = "",
    val url: String = "",
    val htmlUrl: String = "",
    val number: Long = 0,
    val state: State = State.OPEN,
    val title: String = "",
    val description: String = "",
    val createdAt: Instant = Instant.DISTANT_FUTURE,
    val updatedAt: Instant = Instant.DISTANT_FUTURE,
    val closedAt: Instant? = null,
    val dueOn: Instant? = null
)
