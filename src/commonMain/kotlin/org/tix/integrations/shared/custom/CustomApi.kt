package org.tix.integrations.shared.custom

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.tix.net.BaseUrl
import org.tix.serialize.dynamic.DynamicElement

class CustomApi(private val baseUrl: BaseUrl, private val client: HttpClient) {
    suspend fun request(request: CustomRequest): DynamicElement {
        val url = baseUrl.withPath(request.path)
        return client.request(url) {
            method = request.method.toHttpMethod()
            if (request.body.isNotEmpty()) {
                contentType(ContentType.Application.Json)
                setBody(request.body)
            }
            request.parameters.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }
}