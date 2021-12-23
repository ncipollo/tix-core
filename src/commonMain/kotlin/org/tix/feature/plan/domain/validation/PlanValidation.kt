package org.tix.feature.plan.domain.validation

import org.tix.feature.plan.domain.stats.LevelLabels
import org.tix.ticket.Ticket

interface PlanValidation {
    fun validate(tickets: List<Ticket>)
}

fun planValidators(levelLabels: LevelLabels) = listOf(TicketDepthValidation(levelLabels))