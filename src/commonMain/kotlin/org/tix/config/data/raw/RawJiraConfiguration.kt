package org.tix.config.data.raw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tix.config.data.JiraFieldConfiguration

@Serializable
data class RawJiraConfiguration(
    val auth: RawAuthConfiguration? = null,
    @SerialName("no_epics") val noEpics: Boolean? = null,
    val tickets: JiraFieldConfiguration? = null, // Legacy property with weird name
    val fields: JiraFieldConfiguration = tickets ?: JiraFieldConfiguration(), // Legacy property with weird name
    val url: String? = null
)