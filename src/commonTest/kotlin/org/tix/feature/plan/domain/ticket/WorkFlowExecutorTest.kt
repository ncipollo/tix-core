package org.tix.feature.plan.domain.ticket

import kotlinx.coroutines.test.runTest
import org.tix.config.data.Workflow
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class WorkFlowExecutorTest {
    private val env = testEnv()
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
    fun executeWorkFlows() = runTest {
        val results = mapOf(
            "common" to "commonB",
            "A" to "A",
            "B" to "B"
        )
        expect(context.applyResults(results)) {
            workflows.executeWorkFlows(context, env, system)
        }
    }

    @Test
    fun executeWorkFlows_empty() = runTest {
        expect(context) {
            emptyList<Workflow>().executeWorkFlows(context, env, system)
        }
    }
}