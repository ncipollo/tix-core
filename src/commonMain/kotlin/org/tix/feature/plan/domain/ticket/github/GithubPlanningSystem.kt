package org.tix.feature.plan.domain.ticket.github

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.stats.TicketStats
import org.tix.feature.plan.domain.stats.githubTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanningSystem
import org.tix.feature.plan.domain.ticket.github.cache.MilestoneCache
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.github.creator.IssueCreator
import org.tix.feature.plan.domain.ticket.github.creator.IssueLinker
import org.tix.feature.plan.domain.ticket.github.creator.IssueRequestBuilder
import org.tix.feature.plan.domain.ticket.github.creator.ProjectCreator
import org.tix.feature.plan.domain.ticket.github.workflow.GithubWorkflowExecutor
import org.tix.feature.plan.domain.validation.planValidators
import org.tix.integrations.github.GithubApi
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket

class GithubPlanningSystem(
    private val githubApi: GithubApi,
    private val ticketStats: TicketStats,
    private val milestoneCache: MilestoneCache = MilestoneCache(githubApi),
    private val projectCache: ProjectCache = ProjectCache(githubApi),
    private val issueCreator: IssueCreator = IssueCreator(
        githubApi,
        IssueLinker(githubApi, projectCache),
        milestoneCache,
        IssueRequestBuilder(milestoneCache)
    ),
    private val projectCreator: ProjectCreator = ProjectCreator(githubApi, projectCache),
    private val workflowExecutor: GithubWorkflowExecutor = GithubWorkflowExecutor(githubApi, projectCache)
) : TicketPlanningSystem<GithubPlanResult> {

    override suspend fun setup(context: PlanningContext<GithubPlanResult>) {
        issueCreator.setup()
    }

    override suspend fun planTicket(
        context: PlanningContext<GithubPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ): GithubPlanResult {
        val result = if (context.level == 0) {
            projectCreator.planTicket(context, ticket, operation)
        } else {
            issueCreator.planTicket(context, ticket, operation)
        }
        ticketStats.countTicket(context.level)
        return result
    }

    override suspend fun executeWorkFlow(
        workflow: Workflow,
        context: PlanningContext<*>
    ) = workflowExecutor.execute(workflow, context)

    override suspend fun completeInfo() = PlanningCompleteInfo(message = ticketStats.render())

    override suspend fun validate(context: PlanningContext<GithubPlanResult>, tickets: List<Ticket>) {
        val ticketStats = githubTicketStats(startingLevel = context.config.startingLevel)
        planValidators(ticketStats)
            .forEach { it.validate(tickets) }
    }
}