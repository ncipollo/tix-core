package org.tix.config.data

import org.tix.config.data.auth.AuthConfiguration
import org.tix.serialize.dynamic.DynamicElement

interface TicketSystemConfiguration {
    val auth: AuthConfiguration
    val workflows: TicketWorkflows
    fun fieldsForLevel(level: Int): Map<String, DynamicElement>
}