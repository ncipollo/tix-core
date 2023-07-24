package org.tix.feature.plan.domain.ticket.github.workflow

import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.GithubRestApi
import org.tix.integrations.github.rest.issue.IssueApi
import org.tix.integrations.github.rest.repo.RepoApi
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubCloseIssueActionTest {
    private val action = Action(type = "close_issue", arguments = mapOf("issue" to "#1"))
    private val context = PlanningContext<GithubPlanResult>()

    private val issueApi = mockk<IssueApi>(relaxed = true)
    private val githubApi = mockk<GithubApi> {
        every { rest } returns mockk<GithubRestApi> {
            every { repos } returns mockk<RepoApi> {
                every { issues } returns issueApi
            }
        }
    }
    private val githubAction = GithubCloseIssueAction(githubApi)

    @Test
    fun execute() = runTest {
        val result = githubAction.execute(action, context)
        coVerify { issueApi.closeIssue(1) }
        assertEquals(emptyMap(), result)
    }
}