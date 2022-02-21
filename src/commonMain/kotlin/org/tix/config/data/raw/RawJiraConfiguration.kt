package org.tix.config.data.raw

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawJiraConfiguration(
    val auth: RawAuthConfiguration? = null,
    @SerialName("no_epics") val noEpics: Boolean? = null,
    val tickets: RawJiraFieldConfiguration? = null, // Legacy property with weird name
    val fields: RawJiraFieldConfiguration = tickets ?: RawJiraFieldConfiguration(), // Legacy property with weird name
    val url: String? = null,
    val workflows: RawTicketWorkflows? = null
)