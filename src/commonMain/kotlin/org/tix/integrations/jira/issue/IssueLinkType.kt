package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class IssueLinkType(
    val id: String = "",
    val self: String = "",
    val name: String = "",
    val inward: String = "",
    val outward: String = ""
)
