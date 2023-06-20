package org.tix.integrations.github.rest.paging

import io.ktor.util.reflect.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GithubRestPageFetcherTest {
    private val pagingApi = mockk<PagingApi>()
    private val pager = GithubRestPageFetcher(pagingApi)


    @Test
    fun fetchAllPages_multiplePages() = runTest {
        val pages = listOf(
            RestPagedContent(items = listOf("item_1", "item_2"), nextLink = "link_1"),
            RestPagedContent(items = listOf("item_3", "item_4"), nextLink = "link_2"),
            RestPagedContent(items = listOf("item_5"), nextLink = null),
        )

        coEvery { pagingApi.nextPage<String>("link_1", typeInfo<List<String>>()) } returns pages[1]
        coEvery { pagingApi.nextPage<String>("link_2", typeInfo<List<String>>()) } returns pages[2]

        val items = pager.fetchAllPages { pages[0] }
        val expected = listOf("item_1", "item_2", "item_3", "item_4", "item_5")
        assertEquals(expected, items)
    }

    @Test
    fun fetchAllPages_singlePage() = runTest {
        val items = pager.fetchAllPages { RestPagedContent(items = listOf("item_1", "item_2"), nextLink = null) }
        val expected = listOf("item_1", "item_2")
        assertEquals(expected, items)
    }
}