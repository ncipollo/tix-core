package org.tix.feature.plan.domain.ticket.github.creator

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
import org.tix.integrations.github.rest.paging.PagingApi
import org.tix.integrations.github.rest.paging.RestPagedContent
import kotlin.test.expect

class MilestoneFetcherTest {
    private val milestone1 = Milestone(id = 1, title = "1")
    private val milestone2 = Milestone(id = 2, title = "2")

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
                coEvery { repoMilestones() } returns RestPagedContent(
                    items = listOf(milestone1),
                    nextLink = "next"
                )
            }
        }
    }
    private val milestoneFetcher = MilestoneFetcher(githubApi)

    @Test
    fun fetchAllMilestones() = runTest {
        expect(listOf(milestone1, milestone2)) {
            milestoneFetcher.fetchAllMilestones()
        }
    }
}