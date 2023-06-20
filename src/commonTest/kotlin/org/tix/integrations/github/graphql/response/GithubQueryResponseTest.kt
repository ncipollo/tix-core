package org.tix.integrations.github.graphql.response

import org.tix.integrations.github.graphql.pagedRepositoryResponse
import org.tix.integrations.github.graphql.paging.PageInfo
import org.tix.integrations.github.graphql.paging.QueryPagedContent
import kotlin.test.Test
import kotlin.test.expect

class GithubQueryResponseTest {
    private val nodes = listOf("node_1", "node_2")
    private val pageInfo = PageInfo(hasNextPage = true, endCursor = "cursor")

    @Test
    fun pagedContent() {
        val response = pagedRepositoryResponse(nodes, pageInfo)
        expect(QueryPagedContent(nodes, pageInfo)) { response.pagedContent() }
    }
}