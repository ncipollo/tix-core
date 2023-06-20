package org.tix.integrations.github.rest.label

import kotlinx.serialization.Serializable

@Serializable
data class Label(
    val id: Long = 0,
    val nodeId: String = "",
    val url: String = "",
    val name: String = "",
    val description: String = "",
    val color: String? = null,
    val default: Boolean = false
)