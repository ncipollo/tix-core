package org.tix.feature.plan.domain.ticket.jira

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.integrations.jira.JiraApi
import org.tix.integrations.jira.field.FieldApi
import org.tix.integrations.jira.issue.Issue
import org.tix.integrations.jira.issue.IssueApi
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.project.Project
import org.tix.ticket.RenderedTicket
import kotlin.test.assertEquals

class JiraPlanningSystemTest {
    private val issueApi = mockk<IssueApi>()
    private val fieldApi = mockk<FieldApi> {
        coEvery { fields() } returns emptyList()
    }
    private val api = mockk<JiraApi> {
        every { field } returns fieldApi
        every { issue } returns issueApi
    }
    private val planningSystem = JiraPlanningSystem(api)

    @Test
    fun completeInfo() =  runTest {
        val context = PlanningContext<JiraPlanResult>()
        val ticket = RenderedTicket()
        val issue = Issue(fields = IssueFields(type = IssueType(name = "Epic")))

        coEvery { issueApi.create(issue) } returns issue

        planningSystem.setup(context)
        planningSystem.planTicket(context, ticket, PlanningOperation.CreateTicket)
        planningSystem.planTicket(context, ticket, PlanningOperation.CreateTicket)

        val ticketStats = jiraTicketStats().also {
            it.countTicket(0)
            it.countTicket(0)
        }
        val info = planningSystem.completeInfo()
        val expected = PlanningCompleteInfo(message = ticketStats.render())
        assertEquals(expected, info)
    }

    @Test
    fun planTicket_createTicket() = runTest {
        val context = PlanningContext<JiraPlanResult>()
        val ticket = RenderedTicket(
            title = "title",
            body = "body",
            fields = mapOf(
                "project" to "TIX",
            ),
            tixId = "tixId"
        )
        val issue = Issue(
            fields = IssueFields(
                summary = "title",
                description = "body",
                project = Project(key = "TIX"),
                type = IssueType(name = "Epic")
            )
        )
        coEvery { issueApi.create(issue) } returns issue.copy(id = "id", key = "key")

        planningSystem.setup(context)
        val result = planningSystem.planTicket(context, ticket, PlanningOperation.CreateTicket)

        coVerify { issueApi.create(issue) }
        assertEquals(
            expected = JiraPlanResult(
                id = "id",
                key = "key",
                tixId = "tixId",
                description = "title",
                results = mapOf(
                    "ticket.jira.key" to "key",
                    "ticket.jira.previous.key" to "key"
                )
            ),
            actual = result
        )
    }

    @Test
    fun planTicket_deleteTicket() = runTest {
        val context = PlanningContext<JiraPlanResult>()
        val ticket = RenderedTicket(
            title = "title",
            body = "body",
            fields = mapOf(
                "project" to "TIX",
            )
        )
        coEvery { issueApi.delete("key") } returns Unit

        planningSystem.setup(context)
        val result = planningSystem.planTicket(context, ticket, PlanningOperation.DeleteTicket("key"))

        coVerify { issueApi.delete("key") }
        assertEquals(
            expected = JiraPlanResult(
                id = "",
                key = "key",
                description = "title",
                results = mapOf(
                    "ticket.jira.key" to "key",
                    "ticket.jira.previous.key" to "key"
                ),
                operation = PlanningOperation.DeleteTicket("key")
            ),
            actual = result
        )
    }

    @Test
    fun planTicket_updateTicket() = runTest {
        val context = PlanningContext<JiraPlanResult>()
        val ticket = RenderedTicket(
            title = "title",
            body = "body",
            fields = mapOf(
                "project" to "TIX",
            ),
            tixId = "tixId"
        )
        val issue = Issue(
            key = "key",
            fields = IssueFields(
                summary = "title",
                description = "body",
                project = Project(key = "TIX"),
                type = IssueType(name = "Epic")
            )
        )
        coEvery { issueApi.update(issue) } returns issue.copy(id = "id", key = "key")

        planningSystem.setup(context)
        val result = planningSystem.planTicket(context, ticket, PlanningOperation.UpdateTicket("key"))

        coVerify { issueApi.update(issue) }
        assertEquals(
            expected = JiraPlanResult(
                id = "id",
                key = "key",
                tixId = "tixId",
                description = "title",
                results = mapOf(
                    "ticket.jira.key" to "key",
                    "ticket.jira.previous.key" to "key"
                ),
                operation = PlanningOperation.UpdateTicket("key")
            ),
            actual = result
        )
    }
}