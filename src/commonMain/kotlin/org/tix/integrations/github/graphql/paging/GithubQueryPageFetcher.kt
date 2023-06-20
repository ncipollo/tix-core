package org.tix.integrations.github.graphql.paging

object GithubQueryPageFetcher {
    suspend fun <T> fetchAllPages(fetchPage: suspend (cursor: String?) -> QueryPagedContent<T>): List<T> {
        val allNodes = mutableListOf<T>()
        var cursor: String ?= null
        var hasNextPage = true

        while (hasNextPage) {
            val pagedContent = fetchPage(cursor)

            allNodes += pagedContent.nodes
            cursor = pagedContent.pageInfo.endCursor
            hasNextPage = pagedContent.pageInfo.hasNextPage
        }

        return allNodes
    }
}