package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration

interface TicketSystemConfiguration {
    val auth: AuthConfiguration
    val workflows: TicketWorkflows
    val startingLevel: Int
    fun fieldsForLevel(level: Int): Map<String, Any?>
}

fun emptyTicketSystemConfig(): TicketSystemConfiguration = EmptyTicketSystemConfiguration

private object EmptyTicketSystemConfiguration : TicketSystemConfiguration {
    override val auth: AuthConfiguration = AuthConfiguration()
    override val workflows: TicketWorkflows = TicketWorkflows()
    override val startingLevel = 0
    override fun fieldsForLevel(level: Int): Map<String, Any?> = emptyMap()
}