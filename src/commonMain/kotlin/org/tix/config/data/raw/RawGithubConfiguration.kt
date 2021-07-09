package org.tix.config.data.raw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tix.config.data.GithubFieldConfiguration

@Serializable
data class RawGithubConfiguration(
    val auth: RawAuthConfiguration? = null,
    val owner: String? = null,
    val repo: String? = null,
    @SerialName("no_projects") val noProjects: Boolean? = null,
    private val tickets: GithubFieldConfiguration? = null, // Legacy property with weird name
    val fields: GithubFieldConfiguration = tickets ?: GithubFieldConfiguration(),
)

