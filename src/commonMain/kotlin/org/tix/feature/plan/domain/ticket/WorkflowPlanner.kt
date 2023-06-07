package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TicketWorkflows
import org.tix.platform.Env

class WorkflowPlanner<R : TicketPlanResult>(
    private val env: Env,
    private val ticketSystem: TicketPlanningSystem<R>,
    private val workflows: TicketWorkflows,
) {
    suspend fun beforeEach(context: PlanningContext<R>) =
        workflows.beforeEach
            .executeWorkFlows(context, env, ticketSystem)

    suspend fun beforeAll(context: PlanningContext<R>) =
        workflows.beforeAll
            .executeWorkFlows(context, env, ticketSystem)

    suspend fun afterEach(context: PlanningContext<R>) =
        workflows.afterEach
            .executeWorkFlows(context, env, ticketSystem)

    suspend fun afterAll(context: PlanningContext<R>) =
        workflows.afterAll
            .executeWorkFlows(context, env, ticketSystem)
}