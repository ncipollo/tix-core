package org.tix.feature.plan.domain.ticket.jira.workflow

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.config.data.Action
import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import org.tix.integrations.jira.JiraApi
import org.tix.integrations.jira.issue.IssueApi
import kotlin.test.assertEquals

class JiraWorkflowExecutorTest {
    private val issueApi = mockk<IssueApi>(relaxed = true)
    private val jiraApi = mockk<JiraApi> {
        every { issue } returns issueApi
    }
    private val workflow = Workflow(actions = listOf(
        Action(type = "delete_issue", arguments = mapOf("issue" to "issue1")),
        Action(type = "delete_issue", arguments = mapOf("issue" to "issue2")),
    ))
    private val context = mockPlanningContext()

    @Test
    fun execute_withDefaultActionExecutor() = runTest {
        val executor = JiraWorkflowExecutor(jiraApi)
        executor.execute(workflow, context)

        coVerify {
            issueApi.delete("issue1")
            issueApi.delete("issue2")
        }
    }

    @Test
    fun execute_verifyResults() = runTest {
        val actionExecutor = mockk<JiraActionExecutor>() {
            coEvery { execute(workflow.actions[0], context) } returns mapOf("result1" to "one")
            coEvery { execute(workflow.actions[1], context) } returns mapOf("result2" to "two")
        }
        val executor = JiraWorkflowExecutor(jiraApi, actionExecutor = actionExecutor)
        val results = executor.execute(workflow, context)

        val expected = mapOf(
            "result1" to "one",
            "result2" to "two"
        )
        assertEquals(expected, results)
    }
}