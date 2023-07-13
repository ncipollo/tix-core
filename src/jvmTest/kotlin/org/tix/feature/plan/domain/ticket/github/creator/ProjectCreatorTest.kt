package org.tix.feature.plan.domain.ticket.github.creator

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.github.cache.RepositoryCache
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.project.*
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import org.tix.integrations.github.graphql.response.ResponseContent
import org.tix.integrations.github.rest.repo.Repository
import org.tix.integrations.github.rest.user.User
import org.tix.ticket.RenderedTicket
import kotlin.test.expect

class ProjectCreatorTest {
    private val context = PlanningContext<GithubPlanResult>()
    private val repository = Repository(
        nodeId = "repo_1",
        owner = User(nodeId = "owner_2")
    )
    private val ticket = RenderedTicket(
        title = "title",
        body = "body"
    )
    private val project1 = ProjectV2Node(
        id = "1",
        number = 1,
        title = ticket.title,
    )
    private val project2 = ProjectV2Node(
        id = "2",
        number = 2,
        title = ticket.title,
        shortDescription = ticket.body
    )

    private val projectCache = mockk<ProjectCache> {
        coEvery { getProject(1) } returns project1
        coEvery { removeProject(1) } returns Unit
        coEvery { updateProject(project2) } returns Unit
    }
    private val projectQueries = mockk<ProjectQueries> {
        coEvery {
            createProject(repository.nodeId, repository.owner.nodeId, ticket.title)
        } returns GithubQueryResponse(CreateProjectResult(ResponseContent(project1)))

        coEvery {
            deleteProject(project1.id)
        } returns GithubQueryResponse(DeleteProjectResult(ResponseContent(project1)))

        coEvery {
            updateProject(project1.id, ticket.title, ticket.body)
        } returns GithubQueryResponse(UpdateProjectResult(ResponseContent(project2)))
    }
    private val repositoryCache = mockk<RepositoryCache> {
        coEvery { currentRepository() } returns repository
    }

    private val githubApi = mockk<GithubApi> {
        every { queries } returns mockk {
            every { projects } returns projectQueries
        }
    }

    private val projectCreator = ProjectCreator(githubApi, projectCache, repositoryCache)

    @Test
    fun planTicket_createProject() = runTest {
        val operation = PlanningOperation.CreateTicket
        val expectedResult = GithubPlanResult(
            id = project2.id,
            key = "#${project2.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = mapOf(
                "ticket.github.key" to "#2",
                "ticket.github.previous.key" to "#2"
            ),
            operation = operation,
            startingLevel = context.startingLevel
        )
        expect(expectedResult) {
            projectCreator.planTicket(context, ticket, operation)
        }
    }

    @Test
    fun planTicket_deleteProject() = runTest {
        val operation = PlanningOperation.DeleteTicket("#1")
        val expectedResult = GithubPlanResult(
            id = project1.id,
            key = "#${project1.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = mapOf(
                "ticket.github.key" to "#1",
                "ticket.github.previous.key" to "#1"
            ),
            operation = operation,
            startingLevel = context.startingLevel
        )
        expect(expectedResult) {
            projectCreator.planTicket(context, ticket, operation)
        }
    }

    @Test
    fun planTicket_updateProject() = runTest {
        val operation = PlanningOperation.UpdateTicket("#1")
        val expectedResult = GithubPlanResult(
            id = project2.id,
            key = "#${project2.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = mapOf(
                "ticket.github.key" to "#2",
                "ticket.github.previous.key" to "#2"
            ),
            operation = operation,
            startingLevel = context.startingLevel
        )
        expect(expectedResult) {
            projectCreator.planTicket(context, ticket, operation)
        }
    }
}