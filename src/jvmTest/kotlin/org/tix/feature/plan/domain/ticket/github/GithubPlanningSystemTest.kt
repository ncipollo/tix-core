package org.tix.feature.plan.domain.ticket.github

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.config.data.Action
import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.stats.githubTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.github.creator.IssueCreator
import org.tix.feature.plan.domain.ticket.github.creator.ProjectCreator
import org.tix.feature.plan.domain.ticket.github.workflow.GithubWorkflowExecutor
import org.tix.fixture.config.mockGithubConfig
import org.tix.integrations.github.GithubApi
import org.tix.test.runTestWorkaround
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.expect

class GithubPlanningSystemTest {
    private val operation = PlanningOperation.UpdateTicket("#1")
    private val issueContext = PlanningContext<GithubPlanResult>(level = 1)
    private val issueResult = GithubPlanResult(id = "issue", key = "issue_key")
    private val projectContext = PlanningContext<GithubPlanResult>(level = 0)
    private val projectResult = GithubPlanResult(id = "project", key = "project_key")
    private val ticket = RenderedTicket(title = "title", body = "body")
    private val workflow = Workflow(
        actions = listOf(
            Action(type = "close_issue", arguments = mapOf("issue" to "#1")),
            Action(type = "delete_project", arguments = mapOf("project" to "#2")),
        )
    )

    private val githubApi = mockk<GithubApi>()
    private val milestoneCache = mockk<MilestoneCache>()
    private val projectCache = mockk<ProjectCache>()
    private val issueCreator = mockk<IssueCreator> {
        coEvery { planTicket(issueContext, ticket, operation) } returns issueResult
        coEvery { setup() } returns Unit
    }
    private val projectCreator = mockk<ProjectCreator> {
        coEvery { planTicket(projectContext, ticket, operation) } returns projectResult
    }
    private val workflowExecutor = mockk<GithubWorkflowExecutor> {
        coEvery { execute(workflow, issueContext) } returns mapOf("result" to "success")
    }

    @Test
    fun completionInfo_issue() = runTest {
        val planningSystem = planningSystem(issueContext.startingLevel)
        planningSystem.planTicket(issueContext, ticket, operation)
        val expectedMessage = """
            Ticket Stats:
            - Total Tickets: 1
            - Projects: 0
            - Issue: 1
        """.trimIndent()
        expect(PlanningCompleteInfo(message = expectedMessage)) {
            planningSystem.completeInfo()
        }
    }

    @Test
    fun completionInfo_project() = runTest {
        val planningSystem = planningSystem(projectContext.startingLevel)
        planningSystem.planTicket(projectContext, ticket, operation)
        val expectedMessage = """
            Ticket Stats:
            - Total Tickets: 1
            - Project: 1
            - Issues: 0
        """.trimIndent()
        expect(PlanningCompleteInfo(message = expectedMessage)) {
            planningSystem.completeInfo()
        }
    }

    @Test
    fun executeWorkflow() = runTestWorkaround {
        val planningSystem = planningSystem(issueContext.startingLevel)
        expect(mapOf("result" to "success")) {
            planningSystem.executeWorkFlow(workflow, issueContext)
        }
    }

    @Test
    fun planTicket_issue() = runTest {
        val planningSystem = planningSystem(issueContext.startingLevel)
        val result = planningSystem.planTicket(issueContext, ticket, operation)
        assertEquals(issueResult, result)
    }

    @Test
    fun planTicket_project() = runTest {
        val planningSystem = planningSystem(projectContext.startingLevel)
        val result = planningSystem.planTicket(projectContext, ticket, operation)
        assertEquals(projectResult, result)
    }

    @Test
    fun setup() = runTest {
        val planningSystem = planningSystem()
        planningSystem.setup(PlanningContext())
        coVerify { issueCreator.setup() }
    }

    @Test
    fun validate() = runTestWorkaround {
        val planningSystem = planningSystem()
        val context = PlanningContext<GithubPlanResult>(config = mockGithubConfig)
        assertFails {
            planningSystem.validate(context, listOf(deepTicket()))
        }
    }

    private fun deepTicket(currentDepth: Int = 0): Ticket {
        if (currentDepth >= 2) {
            return Ticket()
        }
        return Ticket(children = listOf(deepTicket(currentDepth + 1)))
    }

    private fun planningSystem(startingLevel: Int = 0) = GithubPlanningSystem(
        githubApi = githubApi,
        ticketStats = githubTicketStats(startingLevel),
        milestoneCache = milestoneCache,
        projectCache = projectCache,
        issueCreator = issueCreator,
        projectCreator = projectCreator,
        workflowExecutor = workflowExecutor
    )
}