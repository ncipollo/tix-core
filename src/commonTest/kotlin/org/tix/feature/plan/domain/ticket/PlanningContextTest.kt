package org.tix.feature.plan.domain.ticket

import kotlin.test.Test
import kotlin.test.expect

class PlanningContextTest {
    @Test
    fun applyResults() {
        val context = mockPlanningContext(
            variables = mapOf("common" to "context", "context" to "context")
        )
        val results = mapOf("common" to "results", "results" to "results")

        val merged = mapOf(
            "common" to "results",
            "context" to "context",
            "results" to "results"
        )
        val expected = context.copy(variables = merged)
        expect(expected) {
            context.applyResults(results)
        }
    }

    @Test
    fun createChildContext() {
        val context = mockPlanningContext(
            variables = mapOf("ticket.previous" to "previous", "result" to "parent")
        )
        val result = MockTicketPlanResult(id = "id", results = mapOf("result" to "result"))

        val expected = mockPlanningContext(
            level = 1,
            parentTicket = result,
            variables = mapOf("ticket.parent" to "id", "result" to "parent")
        )
        expect(expected) {
            context.createChildContext(result)
        }
    }

    @Test
    fun createResultContext() {
        val context = mockPlanningContext(
            parentTicket = MockTicketPlanResult(id = "parent"),
            variables = mapOf("original" to "original")
        )
        val result = MockTicketPlanResult(id = "id", results = mapOf("result" to "result"))

        val expected = mockPlanningContext(
            parentTicket = MockTicketPlanResult(id = "parent"),
            variables = mapOf("original" to "original", "result" to "result", "ticket.previous" to "id")
        )
        expect(expected) {
            context.createResultContext(result)
        }
    }
}