package org.tix.config.reader.auth

import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.ticket.system.TicketSystemType

interface AuthSourceReader {
    fun read(
        markdownPath: String,
        rawAuthConfig: RawAuthConfiguration,
        ticketSystemType: TicketSystemType
    ): AuthConfiguration
}