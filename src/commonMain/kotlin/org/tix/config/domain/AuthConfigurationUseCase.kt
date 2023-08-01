package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.reader.auth.AuthSourceReader
import org.tix.domain.FlowTransformer
import org.tix.ticket.system.TicketSystemType

class AuthConfigurationUseCase(
    private val authReader: AuthSourceReader,
) : FlowTransformer<AuthConfigAction, TicketSystemAuth> {

    override fun transformFlow(upstream: Flow<AuthConfigAction>) =
        upstream.map { ticketSystemAuth(it) }

    private fun ticketSystemAuth(action: AuthConfigAction) =
        TicketSystemAuth(
            github = githubAuthConfig(action),
            jira = jiraAuthConfig(action)
        )

    private fun githubAuthConfig(action: AuthConfigAction) =
        authReader.read(action.path,
            action.tixConfig.github?.auth ?: RawAuthConfiguration(),
            TicketSystemType.GITHUB)

    private fun jiraAuthConfig(action: AuthConfigAction) =
        authReader.read(action.path,
            action.tixConfig.jira?.auth ?: RawAuthConfiguration(),
            TicketSystemType.JIRA)
}

data class AuthConfigAction(val path: String? = null, val tixConfig: RawTixConfiguration)