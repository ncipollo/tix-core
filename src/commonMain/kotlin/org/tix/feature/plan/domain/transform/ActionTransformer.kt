package org.tix.feature.plan.domain.transform

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext

fun Action.transform(context: PlanningContext<*>) = copy(arguments = arguments.transform(context))