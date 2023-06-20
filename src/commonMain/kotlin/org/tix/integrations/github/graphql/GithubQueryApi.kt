package org.tix.integrations.github.graphql

import io.ktor.util.reflect.*
import org.tix.integrations.github.graphql.response.GithubQueryResponse

interface GithubQueryApi {
    suspend fun <T> postQuery(query: String, typeInfo: TypeInfo): GithubQueryResponse<T>
}

suspend inline fun <reified T> GithubQueryApi.query(query: String): GithubQueryResponse<T> =
    postQuery(query, typeInfo<GithubQueryResponse<T>>())