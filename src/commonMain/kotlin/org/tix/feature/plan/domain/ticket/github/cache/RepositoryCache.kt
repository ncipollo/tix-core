package org.tix.feature.plan.domain.ticket.github.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.repo.Repository

class RepositoryCache(githubApi: GithubApi) {
    private val mutex = Mutex()
    private val reposApi = githubApi.rest.repos

    private var cachedRepo: Repository? = null

    suspend fun currentRepository() =
        mutex.withLock {
            cachedRepo ?: reposApi.get().also { cachedRepo = it }
        }
}