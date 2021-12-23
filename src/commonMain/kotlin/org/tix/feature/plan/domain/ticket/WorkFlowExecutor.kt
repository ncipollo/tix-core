package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow

internal suspend fun <R : TicketPlanResult> List<Workflow>.executeWorkFlows(
    context: PlanningContext<R>,
    system: TicketPlanningSystem<R>
) = map { system.executeWorkFlow(it, context) }
    .reduce { acc, results -> acc + results }
    .let { context.applyResults(it) }