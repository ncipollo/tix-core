package org.tix.config.reader.auth

import org.tix.model.ticket.system.TicketSystemType

internal object AuthEnvVariables {
    fun forSystemType(ticketSystemType: TicketSystemType) =
        when (ticketSystemType) {
            TicketSystemType.GITHUB -> AuthVariables(password = listOf("GITHUB_API_TOKEN", "GITHUB_PASSWORD"))
            TicketSystemType.JIRA -> AuthVariables(
                user = listOf("JIRA_USERNAME"),
                password = listOf("JIRA_API_TOKEN", "JIRA_PASSWORD")
            )
        }
}

internal data class AuthVariables(val user: List<String> = listOf(""), val password: List<String> = listOf(""))