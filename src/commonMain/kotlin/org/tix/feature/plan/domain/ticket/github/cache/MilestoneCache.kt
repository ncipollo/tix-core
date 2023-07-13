package org.tix.feature.plan.domain.ticket.github.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.milestone.Milestone
import org.tix.integrations.github.rest.milestone.MilestoneCreateRequest
import org.tix.integrations.github.rest.paging.GithubRestPageFetcher

class MilestoneCache(githubApi: GithubApi) {
    private val milestonesByName = mutableMapOf<String, Milestone>()
    private val milestoneApi = githubApi.rest.milestones
    private val mutex = Mutex()
    private val pager = GithubRestPageFetcher(githubApi.rest.paging)

    suspend fun populateCache() {
        val milestones = fetchAllMilestones()
        mutex.withLock {
            milestonesByName.clear()
            milestonesByName += milestones.associateBy { it.title }
        }
    }

    suspend fun getOrCreateMilestone(name: String) =
        mutex.withLock {
            val cached = milestonesByName[name]
            cached ?: createMilestone(name)
                .also { milestonesByName[name] = it }
        }

    private suspend fun fetchAllMilestones() = pager.fetchAllPages { milestoneApi.repoMilestones() }

    private suspend fun createMilestone(name: String) =
        milestoneApi.create(MilestoneCreateRequest(title = name))
}