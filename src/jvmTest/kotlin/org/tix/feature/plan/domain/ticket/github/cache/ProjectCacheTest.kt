package org.tix.feature.plan.domain.ticket.github.cache

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.project.ProjectQueries
import org.tix.integrations.github.graphql.project.ProjectResponse
import org.tix.integrations.github.graphql.project.ProjectV2Node
import org.tix.integrations.github.graphql.project.ProjectV2Wrapper
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import kotlin.test.assertEquals

class ProjectCacheTest {
    private val project1 = ProjectV2Node(id = "1", number = 1, title = "project_1")
    private val project2 = ProjectV2Node(id = "2", number = 2, title = "project_2")

    private val projectQueries = mockk<ProjectQueries> {
        coEvery { repoProject(1) } returns GithubQueryResponse(ProjectResponse(ProjectV2Wrapper(project1)))
    }

    private val githubApi = mockk<GithubApi> {
        every { queries } returns mockk {
            every { projects } returns projectQueries
        }
    }
    private val projectCache = ProjectCache(githubApi)

    @Test
    fun getProject() = runTest {
        val fetchedProject = projectCache.getProject(1)
        coEvery {
            projectQueries.repoProject(1)
        } returns GithubQueryResponse(ProjectResponse(ProjectV2Wrapper(project1)))
        val cachedProject = projectCache.getProject(1)

        assertEquals(project1, fetchedProject)
        assertEquals(project1, cachedProject)
    }

    @Test
    fun updateProject() = runTest {
        projectCache.updateProject(project2)
        val result = projectCache.getProject(2)
        assertEquals(project2, result)
    }
}