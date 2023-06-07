package org.tix.feature.plan.domain.ticket

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.transform.TransformVariableMap
import org.tix.feature.plan.domain.transform.transform
import org.tix.platform.Env

internal suspend fun <R : TicketPlanResult> List<Workflow>.executeWorkFlows(
    context: PlanningContext<R>,
    env: Env,
    system: TicketPlanningSystem<R>
) = takeIf { isNotEmpty() }
    ?.map { it.transform(TransformVariableMap(env, context.variables)) }
    ?.map { system.executeWorkFlow(it, context) }
    ?.reduce { acc, results -> acc + results }
    ?.let { context.applyResults(it) } ?: context