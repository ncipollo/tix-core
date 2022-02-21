package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration

data class JiraConfiguration(
    override val auth: AuthConfiguration,
    val noEpics: Boolean,
    val fields: JiraFieldConfiguration,
    val url: String,
    override val workflows: TicketWorkflows
) : TicketSystemConfiguration {
    override val startingLevel = if(noEpics) 1 else 0

    override fun fieldsForLevel(level: Int): Map<String, Any?> = fields.forLevel(level)
}