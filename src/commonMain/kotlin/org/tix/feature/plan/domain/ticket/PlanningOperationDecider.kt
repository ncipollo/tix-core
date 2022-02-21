package org.tix.feature.plan.domain.ticket

import org.tix.ticket.RenderedTicket

class PlanningOperationDecider(private val ticket: RenderedTicket) {
    fun operation(): PlanningOperation {
        val fields = ticket.fields
        val deleteKey = fields[GenericTicketFields.deleteTicket] as? String
        val updateKey = fields[GenericTicketFields.updateTicket] as? String

        return when {
            deleteKey != null -> PlanningOperation.DeleteTicket(deleteKey)
            updateKey != null -> PlanningOperation.UpdateTicket(updateKey)
            else -> PlanningOperation.CreateTicket
        }
    }
}