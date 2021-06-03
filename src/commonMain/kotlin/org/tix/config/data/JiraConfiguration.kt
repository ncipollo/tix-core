package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty

@Serializable
data class JiraConfiguration(
    @SerialName("no_epics") val noEpics: Boolean? = null,
    val tickets: JiraFieldConfiguration? = null, // Legacy property with weird name
    val fields: JiraFieldConfiguration = tickets ?: JiraFieldConfiguration(), // Legacy property with weird name
    val url: String? = null,
): TicketSystemConfiguration {
    override val isValid = !url.isNullOrBlank()
}

@Serializable
data class JiraFieldConfiguration(
    val default: Map<String, @Contextual DynamicProperty> = mapOf(),
    val epic: Map<String, @Contextual DynamicProperty> = mapOf(),
    val issue: Map<String, @Contextual DynamicProperty> = mapOf(),
    val task: Map<String, @Contextual DynamicProperty> = mapOf(),
)