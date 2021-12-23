package org.tix.config.data

data class Action(
    val type: String = "",
    val label: String = type,
    val arguments: Map<String, Any?> = emptyMap()
)
