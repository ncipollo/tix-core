package org.tix.integrations.github.graphql.repository

import kotlinx.serialization.Serializable
import org.tix.integrations.github.graphql.paging.QueryPagedContent

@Serializable
data class PagedRepositoryResponse<T>(val repository: PagedRepository<T>)

@Serializable
data class PagedRepository<T>(val content: QueryPagedContent<T>)