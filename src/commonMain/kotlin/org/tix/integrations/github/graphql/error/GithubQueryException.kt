package org.tix.integrations.github.graphql.error

data class GithubQueryException(val errorResponse: GithubQueryErrorResponse) :
    RuntimeException("Github Query Errors:\n$errorResponse")
