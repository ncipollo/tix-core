package org.tix.feature.plan.domain.ticket.jira

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.feature.plan.domain.stats.TicketStats
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanningSystem
import org.tix.integrations.jira.JiraApi
import org.tix.integrations.jira.issue.Issue
import org.tix.integrations.jira.issue.IssueApi
import org.tix.ticket.RenderedTicket

class JiraPlanningSystem(private val jiraApi: JiraApi) : TicketPlanningSystem<JiraPlanResult> {
    private val setupMutex = Mutex()
    private lateinit var issueBuilder: JiraIssueBuilder
    private lateinit var ticketStats: TicketStats

    override suspend fun setup(context: PlanningContext<JiraPlanResult>) {
        val fieldCache = jiraApi.field.fieldCache()
        setupMutex.withLock {
            issueBuilder = JiraIssueBuilder(fieldCache)
            ticketStats = jiraTicketStats(context.config.startingLevel)
        }
    }

    override suspend fun executeWorkFlow(
        workflow: Workflow,
        context: PlanningContext<*>
    ) = emptyMap<String, String>()

    override suspend fun completeInfo() = PlanningCompleteInfo(message = ticketStats.render())

    override suspend fun planTicket(
        context: PlanningContext<JiraPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ): JiraPlanResult {
        val issue = issueBuilder.issue(ticket, context, operation)
        val resultIssue = try {
            jiraApi.issue.performOperation(issue, operation)
        } catch (ex: Throwable) {
            throw TicketPlanningException("Jira: $operation operation failed for ${ticket.title}", ex)
        }
        ticketStats.countTicket(context.level)

        return JiraPlanResult(
            id = resultIssue.id,
            key = resultIssue.key,
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = resultIssue.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
    }

    private suspend fun IssueApi.performOperation(issue: Issue, operation: PlanningOperation) =
        when(operation) {
            PlanningOperation.CreateTicket -> create(issue)
            is PlanningOperation.DeleteTicket -> {
                delete(issue.keyOrId)
                issue
            }
            is PlanningOperation.UpdateTicket -> update(issue)
        }
}