package org.tix.integrations.github.graphql.response

import kotlinx.serialization.Serializable
import org.tix.integrations.github.graphql.repository.PagedRepositoryResponse

@Serializable
data class GithubQueryResponse<T>(val data: T)

fun <T> GithubQueryResponse<PagedRepositoryResponse<T>>.pagedContent() = data.repository.content