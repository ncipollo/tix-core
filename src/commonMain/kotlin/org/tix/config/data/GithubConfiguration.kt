package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration
import org.tix.serialize.dynamic.DynamicElement

data class GithubConfiguration(
    override val auth: AuthConfiguration,
    val owner: String,
    val repo: String,
    val noProjects: Boolean,
    val fields: GithubFieldConfiguration,
    override val workflows: TicketWorkflows
) : TicketSystemConfiguration {
    override fun fieldsForLevel(level: Int): Map<String, DynamicElement> =
        if (noProjects) {
            fields.forLevel(level + 1)
        } else {
            fields.forLevel(level)
        }
}

