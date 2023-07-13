package org.tix.feature.plan.domain.ticket.github.creator

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.integrations.github.rest.issue.IssueCreateRequest
import org.tix.integrations.github.rest.issue.IssueUpdateRequest
import org.tix.integrations.github.rest.milestone.Milestone
import org.tix.ticket.RenderedTicket
import kotlin.test.Test
import kotlin.test.expect

class IssueRequestBuilderTest {
    private val milestone1 = Milestone(number = 1, title = "milestone_1")
    private val milestone2 = Milestone(number = 2, title = "milestone_2")

    private val milestoneCache = mockk<MilestoneCache> {
        coEvery { getOrCreateMilestone("milestone_1") } returns milestone1
        coEvery { getOrCreateMilestone("milestone_2") } returns milestone2
    }
    private val issueRequestBuilder = IssueRequestBuilder(milestoneCache)

    @Test
    fun createRequest_emptyFields() = runTest {
        val ticket = RenderedTicket(title = "title", body = "body")
        val expected = IssueCreateRequest(title = ticket.title, body = ticket.body)
        expect(expected) {
            issueRequestBuilder.createRequest(ticket)
        }
    }

    @Test
    fun createRequest_populatedFields() = runTest {
        val ticket = RenderedTicket(
            title = "title",
            body = "body",
            fields = mapOf(
                "assignees" to listOf("user_1", "user_2"),
                "labels" to listOf("label_1", "label_2"),
                "milestone" to "milestone_1"
            )
        )
        val expected = IssueCreateRequest(
            title = ticket.title,
            body = ticket.body,
            assignees = listOf("user_1", "user_2"),
            labels = listOf("label_1", "label_2"),
            milestone = 1
        )
        expect(expected) {
            issueRequestBuilder.createRequest(ticket)
        }
    }

    @Test
    fun updateRequest_emptyFields() = runTest {
        val ticket = RenderedTicket(title = "title", body = "body")
        val expected = IssueUpdateRequest(title = ticket.title, body = ticket.body)
        expect(expected) {
            issueRequestBuilder.updateRequest(ticket)
        }
    }

    @Test
    fun updateRequest_populatedFields() = runTest {
        val ticket = RenderedTicket(
            title = "title",
            body = "body",
            fields = mapOf(
                "assignees" to listOf("user_1", "user_2"),
                "labels" to listOf("label_1", "label_2"),
                "milestone" to "milestone_2"
            )
        )
        val expected = IssueUpdateRequest(
            title = ticket.title,
            body = ticket.body,
            assignees = listOf("user_1", "user_2"),
            labels = listOf("label_1", "label_2"),
            milestone = 2
        )
        expect(expected) {
            issueRequestBuilder.updateRequest(ticket)
        }
    }
}