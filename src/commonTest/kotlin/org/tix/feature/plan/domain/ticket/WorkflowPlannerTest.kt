package org.tix.feature.plan.domain.ticket

import org.tix.fixture.config.workflows
import org.tix.test.platform.testEnv
import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class WorkflowPlannerTest {
    private val afterAllResults = mapOf( "afterAll" to "afterAll")
    private val afterEachResults = mapOf( "afterEach" to "afterEach")
    private val beforeAllResults = mapOf( "beforeAll" to "beforeAll")
    private val beforeEachResults = mapOf( "beforeEach" to "beforeEach")

    private val env = testEnv()
    private val context = mockPlanningContext()
    private val system = MockTicketPlanningSystem().also {
        it.setResultsForWorkflow(workflows.afterAll.first(), afterAllResults)
        it.setResultsForWorkflow(workflows.afterEach.first(), afterEachResults)
        it.setResultsForWorkflow(workflows.beforeAll.first(), beforeAllResults)
        it.setResultsForWorkflow(workflows.beforeEach.first(), beforeEachResults)
    }
    private val planner = WorkflowPlanner(env, system, workflows)

    @Test
    fun afterAll() = runTestWorkaround {
        expect(context.applyResults(afterAllResults)) {
            planner.afterAll(context)
        }
    }

    @Test
    fun afterEach() = runTestWorkaround {
        expect(context.applyResults(afterEachResults)) {
            planner.afterEach(context)
        }
    }

    @Test
    fun beforeAll() = runTestWorkaround {
        expect(context.applyResults(beforeAllResults)) {
            planner.beforeAll(context)
        }
    }

    @Test
    fun beforeEach() = runTestWorkaround {
        expect(context.applyResults(beforeEachResults)) {
            planner.beforeEach(context)
        }
    }
}