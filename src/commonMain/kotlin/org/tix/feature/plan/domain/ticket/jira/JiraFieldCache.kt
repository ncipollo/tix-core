package org.tix.feature.plan.domain.ticket.jira

import org.tix.feature.plan.domain.error.ticketPlanningError
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.field.FieldApi

class JiraFieldCache(fields: List<Field>) {
    private val fieldsByName = fields.associateBy { it.name.lowercase() }

    operator fun get(name: String) = fieldsByName[name.lowercase()]
}

suspend fun FieldApi.fieldCache() =
    try {
        JiraFieldCache(fields())
    } catch (e: Throwable) {
        ticketPlanningError("Failed to populate jira field cache. Error: ${e.message}", e)
    }