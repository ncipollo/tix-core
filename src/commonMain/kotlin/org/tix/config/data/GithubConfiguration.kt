package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration

data class GithubConfiguration(
    override val auth: AuthConfiguration,
    val owner: String,
    val repo: String,
    val noProjects: Boolean,
    val fields: GithubFieldConfiguration,
    override val workflows: TicketWorkflows
) : TicketSystemConfiguration {
    override val startingLevel = if(noProjects) 1 else 0

    override fun fieldsForLevel(level: Int): Map<String, Any?> = fields.forLevel(level)
}

