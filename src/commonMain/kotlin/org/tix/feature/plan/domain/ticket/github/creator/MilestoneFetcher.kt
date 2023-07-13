package org.tix.feature.plan.domain.ticket.github.creator

import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.paging.GithubRestPageFetcher

class MilestoneFetcher(githubApi: GithubApi) {
    private val pager = GithubRestPageFetcher(githubApi.rest.paging)
    private val milestoneApi = githubApi.rest.milestones

    suspend fun fetchAllMilestones() = pager.fetchAllPages { milestoneApi.repoMilestones() }

}