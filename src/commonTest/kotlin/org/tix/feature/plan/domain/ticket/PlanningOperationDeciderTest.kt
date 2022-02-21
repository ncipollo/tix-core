package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.ticket.jira.JiraPlanResult
import org.tix.ticket.RenderedTicket
import kotlin.test.Test
import kotlin.test.expect

class PlanningOperationDeciderTest {
    private val context = PlanningContext<JiraPlanResult>()

    @Test
    fun operation_whenDeleteKeyPresent_returnsDeleteOperation() {
        val ticket = RenderedTicket(fields = mapOf(GenericTicketFields.deleteTicket to "id"))

        expect(PlanningOperation.DeleteTicket("id")) {
            PlanningOperationDecider(ticket).operation()
        }
    }

    @Test
    fun operation_whenDeleteAndUpdateKeysArePresent_returnsDeleteOperation() {
        val ticket = RenderedTicket(
            fields = mapOf(
                GenericTicketFields.deleteTicket to "delete_id",
                GenericTicketFields.updateTicket to "update_id",
            )
        )

        expect(PlanningOperation.DeleteTicket("delete_id")) {
            PlanningOperationDecider(ticket).operation()
        }
    }

    @Test
    fun operation_whenNoSpecialFieldsArePresent_defaultsToCreateOperation() {
        val ticket = RenderedTicket()

        expect(PlanningOperation.CreateTicket) {
            PlanningOperationDecider(ticket).operation()
        }
    }

    @Test
    fun operation_whenUpdateKeyPresent_returnsUpdateOperation() {
        val ticket = RenderedTicket(fields = mapOf(GenericTicketFields.updateTicket to "id"))

        expect(PlanningOperation.UpdateTicket("id")) {
            PlanningOperationDecider(ticket).operation()
        }
    }
}