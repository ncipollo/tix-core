package org.tix.feature.plan.domain.ticket

import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.domain.transform
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.fixture.config.tixConfiguration
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.expect

class TicketPlannerUseCaseTest {
    private val config = tixConfiguration

    private val tickets = listOf(Ticket(title = "a"), Ticket(title = "b"))



    @Test
    fun transform() = runTest {
        val plannerFactory = MockTicketPlannerFactory(2)
        val useCase = TicketPlannerUseCase(plannerFactory)
        val action = TicketPlannerAction(config, shouldDryRun = true, tickets)
        flowOf(action)
            .transform(useCase)
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

    @Test
    fun transform_noConfigs() = runTest {
        val plannerFactory = MockTicketPlannerFactory(0)
        val useCase = TicketPlannerUseCase(plannerFactory)
        val action = TicketPlannerAction(config, shouldDryRun = true, tickets)
        flowOf(action)
            .transform(useCase)
            .test {
                val expected = TicketPlanFailed(TicketPlanningException("no ticket systems configs"))
                expect(expected) { awaitItem() }
                awaitComplete()
                plannerFactory.assertPlannersCalled(shouldDryRun = true, config)
            }
    }

    private suspend fun TurbineTestContext<TicketPlanStatus>.expectTicketCreated(ticketTitle: String) =
        expect(ticketTitle) {
            val status =  this.awaitItem() as TicketPlanUpdated
            status.result.description
        }
}

