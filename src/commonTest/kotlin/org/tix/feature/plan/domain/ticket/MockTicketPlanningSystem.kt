package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.ticket.Ticket
import kotlin.test.assertTrue

internal class MockTicketPlanningSystem : TicketPlanningSystem<MockTicketPlanResult> {
    private var currentId: Int = 1
    private var ticketFailure: TicketFailure? = null
    private var setupCalled = false
    private var workflowResults = mutableMapOf<Workflow, Map<String, String>>()
    private var validationError: TicketPlanningException? = null

    override suspend fun setup() {
        setupCalled = true
    }

    fun assertSetupCalled() {
        assertTrue(setupCalled)
    }

    override suspend fun planTicket(
        context: PlanningContext<MockTicketPlanResult>,
        ticket: Ticket
    ) = if (ticketFailure?.ticket == ticket) {
        throw ticketFailure!!.error
    } else {
        MockTicketPlanResult(
            id = "$currentId",
            level = context.level,
            results = context.variables,
            description = ticket.title
        ).also { currentId++ }
    }

    fun failOnTicket(ticket: Ticket, throwable: Throwable) {
        ticketFailure = TicketFailure(ticket, throwable)
    }

    override suspend fun executeWorkFlow(workflow: Workflow, context: PlanningContext<*>) =
        workflowResults[workflow] ?: emptyMap()

    fun setResultsForWorkflow(workflow: Workflow, results: Map<String, String>) {
        workflowResults[workflow] = results
    }

    override suspend fun completeInfo(): PlanningCompleteInfo = PlanningCompleteInfo(message = "done")

    override suspend fun validate(tickets: List<Ticket>) {
        validationError?.let { throw it }
    }

    fun failValidation(error: TicketPlanningException) {
        validationError = error
    }
}

private data class TicketFailure(val ticket: Ticket, val error: Throwable)