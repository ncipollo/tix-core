package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class WorkFlowExecutorTest {
    private val resultsA = mapOf("common" to "commonA", "A" to "A")
    private val resultsB = mapOf("common" to "commonB", "B" to "B")
    private val workflowA = Workflow("A")
    private val workflowB = Workflow("B")
    private val workflows = listOf(workflowA, workflowB)

    private val context = mockPlanningContext()
    private val system = MockTicketPlanningSystem().also {
        it.setResultsForWorkflow(workflowA, resultsA)
        it.setResultsForWorkflow(workflowB, resultsB)
    }

    @Test
    fun executeWorkFlows() = runTestWorkaround {
        val results = mapOf(
            "common" to "commonB",
            "A" to "A",
            "B" to "B"
        )
        expect(context.applyResults(results)) {
            workflows.executeWorkFlows(context, system)
        }
    }
}