package org.tix.integrations.jira.version

import kotlinx.serialization.Serializable

@Serializable
data class Version(
    val self: String = "",
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val archived: Boolean = false,
    val released: Boolean = false,
    val releaseDate: String = "",
    val userReleaseDate: String = "",
    val projectId: Int = 0,
    val startDate: String = ""
)
