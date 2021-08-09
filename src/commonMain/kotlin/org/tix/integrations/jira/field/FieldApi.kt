package org.tix.integrations.jira.field

import io.ktor.client.*
import io.ktor.client.request.*
import org.tix.net.BaseUrl

class FieldApi(private val baseUrl: BaseUrl, private val client: HttpClient) {
    suspend fun fields(): List<Field> {
        val url = baseUrl.withPath("rest/api/2/field")
        return client.get(url)
    }
}