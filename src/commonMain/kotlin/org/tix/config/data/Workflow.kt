package org.tix.config.data

data class Workflow(
    val label: String = "",
    val actions: List<Action> = emptyList()
)
