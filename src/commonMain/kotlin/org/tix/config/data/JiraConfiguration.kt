package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration
import org.tix.serialize.dynamic.DynamicElement

data class JiraConfiguration(
    override val auth: AuthConfiguration,
    val noEpics: Boolean,
    val fields: JiraFieldConfiguration,
    val url: String,
    override val workflows: TicketWorkflows
) : TicketSystemConfiguration {
    override fun fieldsForLevel(level: Int): Map<String, DynamicElement> =
        if (noEpics) {
            fields.forLevel(level + 1)
        } else {
            fields.forLevel(level)
        }
}