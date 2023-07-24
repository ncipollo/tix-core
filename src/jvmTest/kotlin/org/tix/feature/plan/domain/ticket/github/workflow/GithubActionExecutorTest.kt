package org.tix.feature.plan.domain.ticket.github.workflow

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi
import kotlin.test.Test
import kotlin.test.expect

class GithubActionExecutorTest {
    private val action = Action(type = "close_issue", arguments = mapOf("issue" to "#1"))
    private val context = PlanningContext<GithubPlanResult>()
    private val results = mapOf("result" to "success")

    private val githubApi = mockk<GithubApi>()
    private val projectCache = mockk<ProjectCache>()
    private val githubAction = mockk<GithubAction> {
        coEvery { execute(action, context) } returns results
    }

    private val actionFactory = mockk<GithubActionFactory> {
        every { githubAction(action) } returns githubAction
    }

    private val actionExecutor = GithubActionExecutor(githubApi, projectCache, actionFactory)

    @Test
    fun execute() = runTest {
        expect(results) {
            actionExecutor.execute(action, context)
        }
    }
}