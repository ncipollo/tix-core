package org.tix.integrations.github.graphql.error

import kotlinx.serialization.Serializable

@Serializable
data class QueryError(val message: String = "", val locations: List<QueryErrorLocation> = emptyList()) {
    val errorStrings = locations.map { "- $it $message" }
}
