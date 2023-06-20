package org.tix.integrations.github.rest.paging

class GithubRestPageFetcher(val pagingApi: PagingApi) {
    suspend inline fun <reified T> fetchAllPages(fetchPage: () -> RestPagedContent<T>): List<T> {
        var currentPage = fetchPage()
        val allItems = ArrayList(currentPage.items)
        var nextLink = currentPage.nextLink
        while (nextLink != null) {
            currentPage = pagingApi.nextPage<T>(nextLink)
            allItems += currentPage.items
            nextLink = currentPage.nextLink
        }

        return allItems
    }
}