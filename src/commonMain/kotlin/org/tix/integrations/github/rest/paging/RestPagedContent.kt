package org.tix.integrations.github.rest.paging

import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.util.reflect.*

data class RestPagedContent<T>(val items: List<T> = emptyList(), val nextLink: String? = null)

suspend inline fun <reified T> HttpResponse.pagedContent() =
    pagedContent<T>(typeInfo<List<T>>())

suspend fun <T> HttpResponse.pagedContent(typeInfo: TypeInfo) =
    RestPagedContent(
        items = body<List<T>>(typeInfo),
        nextLink = headers["link"]?.parseNextLink()
    )