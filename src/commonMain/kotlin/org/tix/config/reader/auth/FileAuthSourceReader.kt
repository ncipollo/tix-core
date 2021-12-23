package org.tix.config.reader.auth

import okio.Path.Companion.toPath
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.reader.ConfigurationFileReader
import org.tix.ticket.system.TicketSystemType

internal class FileAuthSourceReader(private val fileReader: ConfigurationFileReader) : AuthSourceReader {
    override fun read(
        markdownPath: String,
        rawAuthConfig: RawAuthConfiguration,
        ticketSystemType: TicketSystemType
    ): AuthConfiguration {
        val paths = AuthConfigurationPaths.searchPaths(markdownPath.toPath(), rawAuthConfig)
        return fileReader.firstConfigFile<AuthConfiguration>(paths) ?: AuthConfiguration()
    }
}