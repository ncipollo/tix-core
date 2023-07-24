package org.tix.feature.plan.domain.ticket.github.workflow

import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.integrations.github.GithubApi

class GithubActionFactory(private val api: GithubApi,
                          private val projectCache: ProjectCache) {
    fun githubAction(action: Action) =
        when(action.type) {
            "close_issue" -> GithubCloseIssueAction(api)
            "delete_project" -> GithubDeleteProjectAction(api, projectCache)
            else -> EmptyGithubAction
        }
}