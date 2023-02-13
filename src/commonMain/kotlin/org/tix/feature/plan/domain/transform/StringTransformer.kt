package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.ticket.PlanningContext

fun String.transform(context: PlanningContext<*>) =
    context.variables.keys
        .scan(this) { acc, key ->
            acc.replace("$$key", context.variables[key]!!)
        }
        .last()