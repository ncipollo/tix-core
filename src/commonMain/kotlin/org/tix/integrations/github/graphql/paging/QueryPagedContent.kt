package org.tix.integrations.github.graphql.paging

import kotlinx.serialization.Serializable

@Serializable
data class QueryPagedContent<T>(val nodes: List<T> = emptyList(), val pageInfo: PageInfo = PageInfo())
