package org.tix.feature.plan.domain.ticket.github.workflow

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.config.data.Action
import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import org.tix.integrations.github.GithubApi
import kotlin.test.assertEquals

class GithubWorkflowExecutorTest {
    private val action1 = Action(type = "close_issue", arguments = mapOf("issue" to "#1"))
    private val action2 = Action(type = "delete_project", arguments = mapOf("project" to "#2"))
    private val context = mockPlanningContext()
    private val githubApi = mockk<GithubApi>(relaxed = true)
    private val actionExecutor = mockk<GithubActionExecutor> {
        coEvery { execute(action1, context) } returns mapOf("result1" to "#1")
        coEvery { execute(action2, context) } returns mapOf("result2" to "#2")
    }
    private val projectCache = mockk<ProjectCache>()
    private val workflowExecutor = GithubWorkflowExecutor(githubApi, projectCache, actionExecutor)
    private val workflow = Workflow(actions = listOf(action1, action2))

    @Test
    fun execute() = runTest {
        val results = workflowExecutor.execute(workflow, context)

        coVerify {
            actionExecutor.execute(workflow.actions[0], context)
            actionExecutor.execute(workflow.actions[1], context)
        }

        val expected = mapOf(
            "result1" to "#1",
            "result2" to "#2"
        )
        assertEquals(expected, results)
    }
}