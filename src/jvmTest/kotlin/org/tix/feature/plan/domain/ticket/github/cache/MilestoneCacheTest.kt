package org.tix.feature.plan.domain.ticket.github.cache

import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.rest.GithubRestApi
import org.tix.integrations.github.rest.milestone.Milestone
import org.tix.integrations.github.rest.milestone.MilestoneApi
import org.tix.integrations.github.rest.milestone.MilestoneCreateRequest
import org.tix.integrations.github.rest.paging.PagingApi
import org.tix.integrations.github.rest.paging.RestPagedContent
import kotlin.test.expect

class MilestoneCacheTest {
    private val milestone1 = Milestone(id = 1, title = "name_1")
    private val milestone2 = Milestone(id = 2, title = "name_2")
    private val milestone3 = Milestone(id = 3, title = "name_3")

    private val githubApi = mockk<GithubApi> {
        every { rest } returns mockk<GithubRestApi> {
            every { paging } returns mockk<PagingApi> {
                coEvery { nextPage<Milestone>("next", typeInfo<List<Milestone>>()) } returns RestPagedContent(
                    listOf(
                        milestone2
                    )
                )
            }
            every { milestones } returns mockk<MilestoneApi> {
                coEvery { create(MilestoneCreateRequest(title = milestone3.title)) } returns milestone3
                coEvery { repoMilestones() } returns RestPagedContent(
                    items = listOf(milestone1),
                    nextLink = "next"
                )
            }
        }
    }
    private val milestoneCache = MilestoneCache(githubApi)

    @Test
    fun populateCache() = runTest {
        milestoneCache.populateCache()

        expect(milestone1) { milestoneCache.getOrCreateMilestone("name_1") }
        expect(milestone2) { milestoneCache.getOrCreateMilestone("name_2") }
    }

    @Test
    fun getOrCreateMilestone() = runTest {
        expect(milestone3) { milestoneCache.getOrCreateMilestone("name_3") }
    }
}