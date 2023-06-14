package org.tix.feature.plan.domain.ticket

import app.cash.turbine.FlowTurbine
import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.fixture.config.jiraConfig
import org.tix.fixture.config.workflows
import org.tix.serialize.dynamic.DynamicElement
import org.tix.test.platform.testEnv
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.expect

class TicketPlannerTest {
    private val env = testEnv()

    private val error = TicketPlanningException("fail")
    private val renderer = jiraBodyRenderer()
    private val system = MockTicketPlanningSystem().also {
        it.setResultsForWorkflow(workflows.beforeAll.first(), mapOf("before_all" to "before_all"))
        it.setResultsForWorkflow(workflows.beforeEach.first(), mapOf("before_each" to "before_each"))
        it.setResultsForWorkflow(workflows.afterAll.first(), mapOf("after_all" to "after_all"))
        it.setResultsForWorkflow(workflows.afterEach.first(), mapOf("after_each" to "after_each"))
    }

    @Test
    fun plan_emptyTickets() = runTest {
        val planner = TicketPlanner(env, renderer, system, jiraConfig, emptyMap(), "$")
        planner.plan(emptyList())
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertPlanningComplete()
                awaitComplete()
            }
    }

    @Test
    fun plan_flatTicketList() = runTest {
        val tickets = listOf(Ticket("1"), Ticket("2"), Ticket("3"))
        val planner = TicketPlanner(env, renderer, system, jiraConfig, emptyMap(), "$")
        planner.plan(tickets)
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertTicketResult(tickets[0], 1)
                assertTicketResult(tickets[1], 2, 1)
                assertTicketResult(tickets[2], 3, 2)
                assertPlanningComplete()
                awaitComplete()
            }
    }

    @Test
    fun plan_flatTicketList_withFailure() = runTest {
        val variables = mapOf("var_key" to "var_value")
        val tickets = listOf(Ticket("1"), Ticket("2"))
        val planner = TicketPlanner(env, renderer, system, jiraConfig, variables, "$")

        system.failOnTicket(RenderedTicket("2", fields = tickets[1].mergedFields(jiraConfig, 0)), error)
        planner.plan(tickets)
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertTicketResult(tickets[0], 1, expectedVariables = variables)
                assertEquals(TicketPlanFailed(error), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun plan_flatTicketList_withUpdateTicket() = runTest {
        val tickets = listOf(Ticket("1", fields = DynamicElement(mapOf(GenericTicketFields.updateTicket to "id"))))
        val planner = TicketPlanner(env, renderer, system, jiraConfig, emptyMap(), "$")
        planner.plan(tickets)
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertTicketResult(tickets[0], 1, expectedOperation = PlanningOperation.UpdateTicket("id"))
                assertPlanningComplete()
                awaitComplete()
            }
    }

    @Test
    fun plan_flatTicketList_withVariables() = runTest {
        val variables = mapOf("var_key" to "var_value")
        val tickets = listOf(Ticket("1"), Ticket("2"))
        val planner = TicketPlanner(env, renderer, system, jiraConfig, variables, "$")
        planner.plan(tickets)
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertTicketResult(tickets[0], 1, expectedVariables = variables)
                assertTicketResult(tickets[1], 2, 1, expectedVariables = variables)
                assertPlanningComplete()
                awaitComplete()
            }
    }

    @Test
    fun plan_nestedTicketList() = runTest {
        val children1 = listOf(Ticket("2"), Ticket("3"))
        val parent1 = Ticket("1", children = children1)
        val children2 = listOf(Ticket("5"), Ticket("6"))
        val parent2 = Ticket("4", children = children2)

        val planner = TicketPlanner(env, renderer, system, jiraConfig, emptyMap(), "$")
        planner.plan(listOf(parent1, parent2))
            .test {
                system.assertSetupCalled()
                assertEquals(TicketPlanStarted, awaitItem())
                assertTicketResult(parent1, 1)
                assertTicketResult(children1[0], 2, parentTicketId = 1)
                assertTicketResult(children1[1], 3, expectedPreviousId = 2, parentTicketId = 1)
                assertTicketResult(parent2, 4, expectedPreviousId = 1)
                assertTicketResult(children2[0], 5, parentTicketId = 4)
                assertTicketResult(children2[1], 6, expectedPreviousId = 5, parentTicketId = 4)
                assertPlanningComplete()
                awaitComplete()
            }
    }

    @Test
    fun plan_validationFails() = runTest {
        val tickets = listOf(Ticket("1"), Ticket("2"))
        val planner = TicketPlanner(env, renderer, system, jiraConfig, emptyMap(), "$")

        system.failValidation(error)
        planner.plan(tickets)
            .test {
                assertEquals(TicketPlanStarted, awaitItem())
                assertEquals(TicketPlanFailed(error), awaitItem())
                awaitComplete()
            }
    }

    private suspend fun FlowTurbine<TicketPlanStatus>.assertTicketResult(
        ticket: Ticket,
        expectedId: Int,
        expectedPreviousId: Int? = null,
        parentTicketId: Int? = null,
        expectedVariables: Map<String, String> = emptyMap(),
        expectedOperation: PlanningOperation = PlanningOperation.CreateTicket
    ) {
        val item = awaitItem()
        assertTrue(item is TicketPlanUpdated)
        (item as? TicketPlanUpdated)?.let {
            assertTrue(it.result is MockTicketPlanResult)
            val result = it.result as MockTicketPlanResult
            assertEquals(expectedId.toString(), result.id)
            assertEquals(ticket.title, result.description)
            if (parentTicketId != null) {
                assertEquals(parentTicketId.toString(), result.results["ticket.parent.id"])
            }

            if (expectedPreviousId != null) {
                assertEquals(expectedPreviousId.toString(), result.results["ticket.previous.id"])
            }

            assertEquals("before_all", result.results["before_all"])
            assertEquals("before_each", result.results["before_each"])
            if (expectedId > 1) {
                assertEquals("after_each", result.results["after_each"])
            }
            expectedVariables.forEach { (key, value) ->
                assertEquals(value, result.results[key])
            }
            assertEquals(expectedOperation, result.operation)
        }
    }

    private suspend fun FlowTurbine<TicketPlanStatus>.assertPlanningComplete() {
        expect(TicketPlanCompleted(system.completeInfo())) {
            awaitItem()
        }
    }
}