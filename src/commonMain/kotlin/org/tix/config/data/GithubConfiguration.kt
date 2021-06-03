package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty

@Serializable
data class GithubConfiguration(
    val owner: String? = null,
    val repo: String? = null,
    @SerialName("no_projects") val noProjects: Boolean? = null,
    private val tickets: GithubFieldConfiguration? = null, // Legacy property with weird name
    val fields: GithubFieldConfiguration = tickets ?: GithubFieldConfiguration(),
) : TicketSystemConfiguration {
    override val isValid = !owner.isNullOrBlank() && !repo.isNullOrBlank()
}

@Serializable
data class GithubFieldConfiguration(
    val default: Map<String, @Contextual DynamicProperty> = mapOf(),
    val project: Map<String, @Contextual DynamicProperty> = mapOf(),
    val issue: Map<String, @Contextual DynamicProperty> = mapOf()
)