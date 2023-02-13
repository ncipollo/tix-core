package org.tix.feature.plan.domain.transform

import org.tix.feature.plan.domain.ticket.PlanningContext

fun Map<String, Any?>.transform(context: PlanningContext<*>) =
    map {(key, value) ->
        key.transform(context) to value.transform(context)
    }.toMap()

private fun Any?.transform(context: PlanningContext<*>) =
    (this as? String)?.transform(context) ?: this