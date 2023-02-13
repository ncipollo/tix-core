package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.transform.transform

internal suspend fun <R : TicketPlanResult> List<Workflow>.executeWorkFlows(
    context: PlanningContext<R>,
    system: TicketPlanningSystem<R>
) = takeIf { isNotEmpty() }
    ?.map { it.transform(context) }
    ?.map { system.executeWorkFlow(it, context) }
    ?.reduce { acc, results -> acc + results }
    ?.let { context.applyResults(it) } ?: context