package org.tix.config.data.raw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawGithubConfiguration(
    val auth: RawAuthConfiguration? = null,
    val owner: String? = null,
    val repo: String? = null,
    @SerialName("no_projects") val noProjects: Boolean? = null,
    private val tickets: RawGithubFieldConfiguration? = null, // Legacy property with weird name
    val fields: RawGithubFieldConfiguration = tickets ?: RawGithubFieldConfiguration(),
    val workflows: RawTicketWorkflows? = null
)

