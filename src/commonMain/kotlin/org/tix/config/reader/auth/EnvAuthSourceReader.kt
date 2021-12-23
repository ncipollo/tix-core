package org.tix.config.reader.auth

import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.ticket.system.TicketSystemType
import org.tix.platform.Env

class EnvAuthSourceReader(private val env: Env) : AuthSourceReader {
    override fun read(
        markdownPath: String,
        rawAuthConfig: RawAuthConfiguration,
        ticketSystemType: TicketSystemType
    ): AuthConfiguration {
        val variables = AuthEnvVariables.forSystemType(ticketSystemType)
        return AuthConfiguration(
            username = firstEnvVariable(variables.user),
            password = firstEnvVariable(variables.password)
        )
    }

    private fun firstEnvVariable(names: List<String>) =
        names.map { env[it] }
            .firstOrNull { it.isNotBlank() } ?: ""
}