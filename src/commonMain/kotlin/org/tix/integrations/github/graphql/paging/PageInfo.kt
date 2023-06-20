package org.tix.integrations.github.graphql.paging

import kotlinx.serialization.Serializable

@Serializable
data class PageInfo(val hasNextPage: Boolean = false, val endCursor: String = "")