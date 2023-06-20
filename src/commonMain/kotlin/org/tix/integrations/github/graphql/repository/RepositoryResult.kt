package org.tix.integrations.github.graphql.repository

import kotlinx.serialization.Serializable

@Serializable
data class RepositoryResult<T>(val repository: T)
