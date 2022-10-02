package org.tix.feature.plan.domain.parse

import org.tix.ticket.Ticket

class TixIdGenerator(startingId: Long = 1) {
    private var currentId = startingId

    fun attachIdsToTickets(tickets: List<Ticket>): List<Ticket> =
        tickets.map {
            val tixId = "tix_$currentId"
            currentId++
            it.copy(
                tixId = tixId,
                children = attachIdsToTickets(it.children)
            )
        }
}