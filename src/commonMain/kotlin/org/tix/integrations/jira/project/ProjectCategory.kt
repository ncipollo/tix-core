package org.tix.integrations.jira.project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectCategory(
    val self: String = "",
    val id: String = "",
    val name: String = "",
    val description: String = ""
)
