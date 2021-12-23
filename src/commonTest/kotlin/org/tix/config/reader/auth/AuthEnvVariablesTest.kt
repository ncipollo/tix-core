package org.tix.config.reader.auth

import org.tix.ticket.system.TicketSystemType
import kotlin.test.Test
import kotlin.test.expect

class AuthEnvVariablesTest {
    @Test
    fun forSystemType_whenTypeIsGithub_returnsGithubVariables() {
        val expected = AuthVariables(password = listOf("GITHUB_API_TOKEN", "GITHUB_PASSWORD"))
        expect(expected) { AuthEnvVariables.forSystemType(TicketSystemType.GITHUB) }
    }

    @Test
    fun forSystemType_whenTypeIsJira_returnsJiraVariables() {
        val expected = AuthVariables(
            user = listOf("JIRA_USERNAME"),
            password = listOf("JIRA_API_TOKEN", "JIRA_PASSWORD")
        )
        expect(expected) { AuthEnvVariables.forSystemType(TicketSystemType.JIRA) }
    }
}