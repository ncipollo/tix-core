package org.tix.integrations.github.graphql.error

import kotlinx.serialization.Serializable

@Serializable
data class QueryErrorLocation(val line: Int = 0, val column: Int = 0) {
    override fun toString(): String = "[$line:$column]"

}