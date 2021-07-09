package org.tix.config.reader.auth

import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.model.ticket.system.TicketSystemType
import org.tix.platform.PlatformEnv

class AuthReader(
    private val fileReader: AuthSourceReader,
    private val envReader: AuthSourceReader = EnvAuthSourceReader(PlatformEnv)
) : AuthSourceReader {
    override fun read(
        markdownPath: String,
        rawAuthConfig: RawAuthConfiguration,
        ticketSystemType: TicketSystemType
    ): AuthConfiguration =
        when(authSource(rawAuthConfig)) {
            AuthSource.ENV -> envReader.read(markdownPath, rawAuthConfig, ticketSystemType)
            AuthSource.LOCAL_FILE, AuthSource.TIX_FILE ->
                fileReader.read(markdownPath, rawAuthConfig, ticketSystemType)
        }

    private fun authSource(rawAuthConfig: RawAuthConfiguration) = rawAuthConfig.source ?: AuthSource.ENV
}