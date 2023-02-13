package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Action

interface JiraAction {
    suspend fun execute(action: Action): Map<String, String>
}