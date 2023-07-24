package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi

class GithubActionExecutor(
    githubApi: GithubApi,
    projectCache: ProjectCache,
    private val actionFactory: GithubActionFactory = GithubActionFactory(githubApi, projectCache)
) {
    suspend fun execute(action: Action, context: PlanningContext<*>) =
        actionFactory.githubAction(action)
            .execute(action, context)
}