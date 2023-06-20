package org.tix.integrations.github.graphql.response

import kotlinx.serialization.Serializable

@Serializable
data class ResponseContent<T>(val content: T)
