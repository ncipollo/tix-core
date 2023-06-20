package org.tix.integrations.github.rest.paging

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.util.reflect.*

class PagingApi(val client: HttpClient) {
    suspend inline fun <reified T> nextPage(url: String) =
        nextPage<T>(url, typeInfo<List<T>>())

    suspend fun <T> nextPage(url: String, typeInfo: TypeInfo) =
        client.get(url).pagedContent<T>(typeInfo)
}