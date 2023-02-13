package org.tix.feature.plan.domain.transform

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.PlanningContext

fun Workflow.transform(context: PlanningContext<*>) = copy(actions = actions.map { it.transform(context) })