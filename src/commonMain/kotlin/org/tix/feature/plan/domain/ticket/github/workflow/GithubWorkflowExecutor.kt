package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Workflow
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi

class GithubWorkflowExecutor(
    githubApi: GithubApi,
    projectCache: ProjectCache,
    private val actionExecutor: GithubActionExecutor = GithubActionExecutor(githubApi, projectCache)
) {
    suspend fun execute(workflow: Workflow, context: PlanningContext<*>) =
        workflow.actions
            .map { actionExecutor.execute(it, context) }
            .reduce { acc, results -> acc + results }
}