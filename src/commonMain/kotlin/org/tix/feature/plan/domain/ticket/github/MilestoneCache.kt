package org.tix.feature.plan.domain.ticket.github

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tix.integrations.github.rest.milestone.Milestone
import org.tix.integrations.github.rest.milestone.MilestoneApi
import org.tix.integrations.github.rest.paging.GithubRestPageFetcher
import org.tix.integrations.github.state.StateQuery

class MilestoneCache(private val milestoneApi: MilestoneApi,
                     private val pager: GithubRestPageFetcher) {
    private val mutex = Mutex()
    private val milestonesByName = mutableMapOf<String, Milestone>()

    suspend fun populateCache() {
        val milestones = fetchAllMilestones()
        mutex.withLock {
            milestonesByName.clear()
            milestonesByName += milestones.associateBy { it.title }
        }
    }

    suspend fun createMilestoneIfNeeded(milestone: Milestone) {
        var alreadyExists = false
        mutex.withLock {
            alreadyExists = milestonesByName.containsKey(milestone.title)
        }

        if (alreadyExists) {
            return
        }

        // TODO: Create Milestone if needed
        mutex.withLock {
            milestonesByName[milestone.title] = milestone
        }
    }

    private suspend fun fetchAllMilestones() =
        pager.fetchAllPages { milestoneApi.repoMilestones(state = StateQuery.ALL) }
}