package org.tix.integrations.github.rest.milestone

import kotlinx.serialization.Serializable

@Serializable
data class MilestoneCreateResponse(
    val id: Long = 0,
    val number: Long = 0,
    val nodeId: String = ""
)
