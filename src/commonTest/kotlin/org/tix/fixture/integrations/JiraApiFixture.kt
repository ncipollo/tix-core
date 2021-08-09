package org.tix.fixture.integrations

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.JiraFieldConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.reader.auth.EnvAuthSourceReader
import org.tix.integrations.jira.JiraApi
import org.tix.model.ticket.system.TicketSystemType
import org.tix.platform.PlatformEnv

fun jiraApi() = JiraApi(jiraConfig())

private fun jiraConfig() =
    JiraConfiguration(
        auth = jiraAuth(),
        noEpics = false,
        fields = JiraFieldConfiguration(),
        url = "https://tix-test.atlassian.net"
    )

private fun jiraAuth() =
    EnvAuthSourceReader(PlatformEnv)
        .read("", RawAuthConfiguration(), TicketSystemType.JIRA)
