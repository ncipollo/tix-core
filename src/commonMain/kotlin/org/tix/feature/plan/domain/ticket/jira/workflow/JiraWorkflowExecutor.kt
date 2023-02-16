package org.tix.feature.plan.domain.ticket.jira.workflow

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.JiraApi

class JiraWorkflowExecutor(
    jiraApi: JiraApi,
    private val actionExecutor: JiraActionExecutor = JiraActionExecutor(jiraApi)
) {
    suspend fun execute(workflow: Workflow, context: PlanningContext<*>) =
        workflow.actions
            .map { actionExecutor.execute(it, context) }
            .reduce { acc, results -> acc + results }
}