package org.tix.config.data.raw

import kotlinx.serialization.Serializable

@Serializable
data class RawWorkflow(
    val label: String = "",
    val actions: List<RawAction> = emptyList()
)
