package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.ticketNumber
import org.tix.integrations.github.GithubApi

class GithubDeleteProjectAction(
    private val api: GithubApi,
    private val projectCache: ProjectCache
): GithubAction {
    override suspend fun execute(action: Action, context: PlanningContext<*>): Map<String, String> {
        val projectNumber = ticketNumber(action.arguments["project"])
        val projectId = projectCache.getProject(projectNumber).id
        api.queries.projects.deleteProject(projectId)
        return emptyMap()
    }
}