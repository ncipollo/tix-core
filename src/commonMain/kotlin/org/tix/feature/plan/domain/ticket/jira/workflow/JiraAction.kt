package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext

interface JiraAction {
    suspend fun execute(action: Action, context: PlanningContext<*>): Map<String, String>
}

