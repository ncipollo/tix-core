package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Component(
    val self: String = "",
    val id: String = "",
    val name: String = ""
)
