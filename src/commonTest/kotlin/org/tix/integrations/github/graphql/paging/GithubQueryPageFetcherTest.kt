package org.tix.integrations.github.graphql.paging

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GithubQueryPageFetcherTest {
    @Test
    fun fetchAllPages_multiplePages() = runTest {
        var index = 0
        val pages = listOf(
            QueryPagedContent(
                nodes = listOf("node_1", "node_2"),
                pageInfo = PageInfo(hasNextPage = true, endCursor = "cursor_1")
            ),
            QueryPagedContent(
                nodes = listOf("node_3", "node_4"),
                pageInfo = PageInfo(hasNextPage = true, endCursor = "cursor_2")
            ),
            QueryPagedContent(
                nodes = listOf("node_5"),
                pageInfo = PageInfo(hasNextPage = false, endCursor = "cursor_2")
            )
        )

        val expectedCursors = listOf(null, "cursor_1", "cursor_2")
        val nodes = GithubQueryPageFetcher.fetchAllPages { cursor ->
            pages[index].also {
                assertEquals(expectedCursors[index], cursor)
                index++
            }
        }

        val expectedNodes = listOf("node_1", "node_2", "node_3", "node_4", "node_5")
        assertEquals(expectedNodes, nodes)
    }

    @Test
    fun fetchAllPages_singlePage() = runTest {
        val page = QueryPagedContent(
            nodes = listOf("node_1", "node_2"),
            pageInfo = PageInfo(hasNextPage = false, endCursor = "cursor_1")
        )

        val nodes = GithubQueryPageFetcher.fetchAllPages { page }

        val expectedNodes = listOf("node_1", "node_2")
        assertEquals(expectedNodes, nodes)
    }
}