package org.tix.integrations.github.graphql

import org.tix.integrations.github.graphql.paging.PageInfo
import org.tix.integrations.github.graphql.paging.QueryPagedContent
import org.tix.integrations.github.graphql.repository.PagedRepository
import org.tix.integrations.github.graphql.repository.PagedRepositoryResponse
import org.tix.integrations.github.graphql.response.GithubQueryResponse

fun <T> pagedRepositoryResponse(nodes: List<T>, pageInfo: PageInfo = PageInfo()) =
    GithubQueryResponse(
        PagedRepositoryResponse(
            PagedRepository(
                QueryPagedContent(
                    nodes = nodes,
                    pageInfo = pageInfo
                )
            )
        )
    )