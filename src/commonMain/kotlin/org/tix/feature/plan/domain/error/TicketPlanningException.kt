package org.tix.feature.plan.domain.error

data class TicketPlanningException(
    override val message: String = "",
    override val cause: Throwable? = null
) : RuntimeException(message, cause)

fun ticketPlanningError(message: String, cause: Throwable? = null): Nothing =
    throw TicketPlanningException(message, cause)