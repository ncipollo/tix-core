package org.tix.feature.plan.parse.state

internal class ParserState {
    private val ticketPath: MutableList<PartialTicket> = ArrayList()

    val currentTicket get() = ticketPath.lastOrNull()
    val rootTickets: MutableList<PartialTicket> = ArrayList()
    val ticketLevel get() = ticketPath.size
    val ticketNeedsTitle get() = currentTicket?.title?.isBlank() ?: false

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
}
