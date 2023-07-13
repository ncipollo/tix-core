package org.tix.feature.plan.domain.ticket.github.cache

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.repo.RepoApi
import org.tix.integrations.github.rest.repo.Repository
import kotlin.test.assertEquals

class RepositoryCacheTest {
    private val repo1 = Repository(id = 1, nodeId = "1", name = "repo_1")
    private val repo2 = Repository(id = 2, nodeId = "2", name = "repo_2")

    private val reposApi = mockk<RepoApi> {
        coEvery { get() } returns repo1
    }
    private val githubApi = mockk<GithubApi> {
        every { rest } returns mockk {
            every { repos } returns reposApi
        }
    }
    private val repoCache = RepositoryCache(githubApi)

    @Test
    fun currentRepository() = runTest {
        val fetchedResult = repoCache.currentRepository()
        coEvery { reposApi.get() } returns repo2
        val cachedResult = repoCache.currentRepository()

        assertEquals(repo1, fetchedResult)
        assertEquals(repo1, cachedResult)
    }
}