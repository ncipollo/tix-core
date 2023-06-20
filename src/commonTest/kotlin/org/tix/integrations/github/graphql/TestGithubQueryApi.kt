package org.tix.integrations.github.graphql

import io.ktor.util.reflect.*
import org.tix.integrations.github.graphql.repository.PagedRepositoryResponse
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import kotlin.test.assertEquals

class TestGithubQueryApi : GithubQueryApi {
    private var query: String? = null
    private var typeInfo: TypeInfo? = null

    private var response: GithubQueryResponse<*>? = null

    fun <T> preparePagedResponse(pagedResponse: GithubQueryResponse<PagedRepositoryResponse<T>>) {
        response = pagedResponse
    }

    fun <T> prepareResponse(response: GithubQueryResponse<T>) {
        this.response = response
    }

    override suspend fun <T> postQuery(query: String, typeInfo: TypeInfo): GithubQueryResponse<T> {
        this.query = query
        this.typeInfo = typeInfo

        @Suppress("UNCHECKED_CAST")
        return response as GithubQueryResponse<T>
    }

    fun assertQuery(expectedQuery: String) = assertEquals(expectedQuery, query)

    fun assertTypeInfo(expectedTypeInfo: TypeInfo) = assertEquals(expectedTypeInfo, typeInfo)

    inline fun <reified T> assertReturnType() = assertTypeInfo(typeInfo<T>())
}