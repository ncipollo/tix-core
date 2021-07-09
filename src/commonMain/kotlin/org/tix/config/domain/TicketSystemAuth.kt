package org.tix.config.domain

import org.tix.config.data.auth.AuthConfiguration

data class TicketSystemAuth(
    val github: AuthConfiguration = AuthConfiguration(),
    val jira: AuthConfiguration = AuthConfiguration()
)