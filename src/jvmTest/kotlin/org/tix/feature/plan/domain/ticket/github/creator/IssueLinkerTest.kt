package org.tix.feature.plan.domain.ticket.github.creator

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.GithubTicketSystemFields
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.GithubQueries
import org.tix.integrations.github.graphql.project.AddItemToProjectResult
import org.tix.integrations.github.graphql.project.ItemIdNode
import org.tix.integrations.github.graphql.project.ProjectQueries
import org.tix.integrations.github.graphql.project.ProjectV2Node
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import org.tix.integrations.github.graphql.response.ResponseContent
import org.tix.integrations.github.rest.issue.Issue
import org.tix.ticket.RenderedTicket

class IssueLinkerTest {
    private val ticket = RenderedTicket(
        title = "title",
        body = "body"
    )
    private val project1 = ProjectV2Node(
        id = "project_1",
        number = 1,
        title = ticket.title,
    )
    private val project2 = ProjectV2Node(
        id = "project_2",
        number = 2,
        title = ticket.title,
    )
    private val issue = Issue(id = 1, nodeId = "issue_1")

    private val context = PlanningContext(parentTicket = GithubPlanResult(id = project1.id))
    private val projectQueries = mockk<ProjectQueries> {
        coEvery { addItemToProject(project1.id, issue.nodeId) } returns GithubQueryResponse(
            AddItemToProjectResult(ResponseContent(ItemIdNode(issue.nodeId)))
        )
        coEvery { addItemToProject(project2.id, issue.nodeId) } returns GithubQueryResponse(
            AddItemToProjectResult(ResponseContent(ItemIdNode(issue.nodeId)))
        )
    }
    private val githubApi = mockk<GithubApi> {
        every { queries } returns mockk<GithubQueries> {
            every { projects } returns projectQueries
        }
    }
    private val projectCache = mockk<ProjectCache> {
        coEvery { getProject(2) } returns project2
    }
    private val issueLinker = IssueLinker(githubApi, projectCache)

    @Test
    fun linkIssueToProject_parentFromContext() = runTest {
        issueLinker.linkIssueToProject(context, issue, ticket)
        coVerify { projectQueries.addItemToProject(project1.id, issue.nodeId) }
    }

    @Test
    fun linkIssueToProject_parentFromTicket() = runTest {
        val ticketWithProject = ticket.copy(fields = mapOf(GithubTicketSystemFields.parent to "#2"))
        issueLinker.linkIssueToProject(context, issue, ticketWithProject)
        coVerify { projectQueries.addItemToProject(project2.id, issue.nodeId) }
    }
}