package org.tix.feature.plan.domain.ticket.jira.workflow

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import kotlin.test.expect

class JiraActionExecutorTest {
    private val action = Action(type = "delete_issue", arguments = mapOf("issue" to "issue1"))
    private val context = mockPlanningContext()

    private val jiraAction = mockk<JiraAction> {
        coEvery { execute(action, context) } returns mapOf("key" to "result")
    }
    private val actionFactory = mockk<JiraActionFactory> {
        every { jiraAction(action) } returns jiraAction
    }

    private val executor = JiraActionExecutor(mockk(), actionFactory)

    @Test
    fun execute_deleteAction() = runTest {
        val action = Action(type = "delete_issue", arguments = mapOf("issue" to "issue1"))

        expect(mapOf("key" to "result")) {
            executor.execute(action, context)
        }
    }
}