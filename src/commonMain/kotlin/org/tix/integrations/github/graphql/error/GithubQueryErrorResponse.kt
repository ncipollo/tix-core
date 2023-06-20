package org.tix.integrations.github.graphql.error

import kotlinx.serialization.Serializable

@Serializable
data class GithubQueryErrorResponse(val errors: List<QueryError> = emptyList()) {
    override fun toString() = errors.flatMap { it.errorStrings }.joinToString("\n")
}
