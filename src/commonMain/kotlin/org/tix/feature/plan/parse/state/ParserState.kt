package org.tix.feature.plan.parse.state

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.LinebreakSegment

internal class ParserState {
    private val ticketPath: MutableList<PartialTicket> = ArrayList()

    val currentTicket get() = ticketPath.lastOrNull()
    val listState = ListState()
    val rootTickets: MutableList<PartialTicket> = ArrayList()
    val ticketLevel get() = ticketPath.size

    fun startTicket() {
        val newTicket = PartialTicket()
        // TODO: Add field state support
        currentTicket?.children?.add(newTicket)
        ticketPath.add(newTicket)
    }

    fun completeTicket() {
        if (ticketLevel == 1) {
            currentTicket?.let { rootTickets.add(it) }
        }
        ticketPath.removeLastOrNull()
    }

    fun completeAllTickets() {
        while (currentTicket != null) {
            completeTicket()
        }
    }

    fun addBodySegments(vararg segments: BodySegment) = currentTicket?.addSegments(segments)

    fun addBodyLinebreak() = addBodySegments(LinebreakSegment)

    fun buildNestedBody(buildBlock: () -> Unit) : List<BodySegment> =
        currentTicket?.buildNestedBody(buildBlock) ?: error("no ticket has been started")
}
