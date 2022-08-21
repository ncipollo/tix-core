package org.tix.feature.plan.domain.ticket

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.tix.domain.transform
import org.tix.fixture.config.tixConfiguration
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.expect

class TicketPlannerUseCaseTest {
    private val config = tixConfiguration
    private val plannerFactory = MockTicketPlannerFactory(2)
    private val tickets = listOf(Ticket(title = "a"), Ticket(title = "b"))

    private val useCase = TicketPlannerUseCase(plannerFactory)

    @Test
    fun transform() = runTest {
        val action = TicketPlannerAction(config, shouldDryRun = true, tickets)
        flowOf(action)
            .transform(useCase)
            .onEach { println(it) }
            .test {
                repeat(2) {
                    expect(TicketPlanStarted) { awaitItem() }
                    expectTicketCreated("a")
                    expectTicketCreated("b")
                    expect(TicketPlanCompleted(PlanningCompleteInfo(message = "done"))) { awaitItem() }
                }
                awaitComplete()
                plannerFactory.assertPlannersCalled(shouldDryRun = true, config)
            }
    }

    private suspend fun FlowTurbine<TicketPlanStatus>.expectTicketCreated(ticketTitle: String) =
        expect(ticketTitle) {
            val status =  this.awaitItem() as TicketPlanUpdated
            status.result.description
        }
}

