package org.tix.feature.plan.domain.ticket.github.creator

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.feature.plan.domain.ticket.github.result.resultMap
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.GithubRestApi
import org.tix.integrations.github.rest.issue.Issue
import org.tix.integrations.github.rest.issue.IssueApi
import org.tix.integrations.github.rest.issue.IssueCreateRequest
import org.tix.integrations.github.rest.issue.IssueUpdateRequest
import org.tix.ticket.RenderedTicket
import kotlin.test.assertEquals

class IssueCreatorTest {
    private val context = PlanningContext<GithubPlanResult>(startingLevel = 1)
    private val ticket = RenderedTicket(title = "title", body = "body")
    private val createRequest = IssueCreateRequest(title = ticket.title, body = ticket.body)
    private val updateRequest = IssueUpdateRequest(title = ticket.title, body = ticket.body)
    private val createdIssue = Issue(id = 1, nodeId = "1", title = "created")
    private val closedIssue = Issue(id = 2, nodeId = "2", title = "deleted")
    private val updatedIssue = Issue(id = 3, nodeId = "3", title = "updated")

    private val issuesApi = mockk<IssueApi> {
        coEvery { create(createRequest) } returns createdIssue
        coEvery { closeIssue(closedIssue.id) } returns closedIssue
        coEvery { update(updatedIssue.id, updateRequest) } returns updatedIssue
    }
    private val githubApi = mockk<GithubApi> {
        every { rest } returns mockk<GithubRestApi> {
            coEvery { issues } returns issuesApi
        }
    }
    private val issueLinker = mockk<IssueLinker>(relaxed = true)
    private val milestoneCache = mockk<MilestoneCache>(relaxed = true)
    private val requestBuilder = mockk<IssueRequestBuilder> {
        coEvery { createRequest(ticket) } returns createRequest
        coEvery { updateRequest(ticket) } returns updateRequest
    }
    private val issueCreator = IssueCreator(githubApi, issueLinker, milestoneCache, requestBuilder)

    @Test
    fun performOperation_closeIssue() = runTest {
        val operation = PlanningOperation.DeleteTicket("#2")
        val result = issueCreator.planTicket(context, ticket, operation)

        val expectedResult = GithubPlanResult(
            id = closedIssue.nodeId,
            key = "#${closedIssue.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = closedIssue.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
        assertEquals(expectedResult, result)
    }

    @Test
    fun performOperation_createIssue() = runTest {
        val operation = PlanningOperation.CreateTicket
        val result = issueCreator.planTicket(context, ticket, operation)

        val expectedResult = GithubPlanResult(
            id = createdIssue.nodeId,
            key = "#${createdIssue.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = createdIssue.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
        coVerify { issueLinker.linkIssueToProject(context, createdIssue, ticket) }
        assertEquals(expectedResult, result)
    }

    @Test
    fun performOperation_updateIssue() = runTest {
        val operation = PlanningOperation.UpdateTicket("#3")
        val result = issueCreator.planTicket(context, ticket, operation)

        val expectedResult = GithubPlanResult(
            id = updatedIssue.nodeId,
            key = "#${updatedIssue.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = updatedIssue.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
        assertEquals(expectedResult, result)
    }

    @Test
    fun setup() = runTest {
        issueCreator.setup()
        coVerify { milestoneCache.populateCache() }
    }
}