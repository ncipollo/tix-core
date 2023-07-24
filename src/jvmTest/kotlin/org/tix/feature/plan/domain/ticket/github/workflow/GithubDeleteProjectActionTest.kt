package org.tix.feature.plan.domain.ticket.github.workflow

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.GithubQueries
import org.tix.integrations.github.graphql.project.ProjectQueries
import org.tix.integrations.github.graphql.project.ProjectV2Node
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubDeleteProjectActionTest {
    private val action = Action(type = "delete_project", arguments = mapOf("project" to "#1"))
    private val context = PlanningContext<GithubPlanResult>()
    private val project = ProjectV2Node(id = "id", number = 1)

    private val projectQueries = mockk<ProjectQueries>(relaxed = true)
    private val githubApi = mockk<GithubApi> {
        every { queries } returns mockk<GithubQueries> {
            every { projects } returns projectQueries
        }
    }
    private val projectCache = mockk<ProjectCache> {
        coEvery { getProject(1) } returns project
    }
    private val githubAction = GithubDeleteProjectAction(githubApi, projectCache)

    @Test
    fun execute() = runTest {
        val result = githubAction.execute(action, context)
        coVerify { projectQueries.deleteProject(project.id) }
        assertEquals(emptyMap(), result)
    }
}