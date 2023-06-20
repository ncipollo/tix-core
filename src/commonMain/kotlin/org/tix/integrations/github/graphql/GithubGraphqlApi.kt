package org.tix.integrations.github.graphql

import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.reflect.*
import kotlinx.serialization.Serializable
import org.tix.config.data.GithubConfiguration
import org.tix.integrations.github.graphql.response.GithubQueryResponse
import org.tix.net.http.httpClient
import org.tix.net.http.installContentNegotiation

class GithubGraphqlApi(configuration: GithubConfiguration, engine: HttpClientEngine? = null): GithubQueryApi {
    private val client = httpClient(engine) {
        installContentNegotiation()
        defaultRequest {
            accept(ContentType("application", "vnd.github+json"))
            bearerAuth(configuration.auth.password)
            header("X-GitHub-Api-Version", "2022-11-28")
        }
        HttpResponseValidator {
// There is currently a KTOR bug with validation, this would consume the body regardless of error:
// See https://youtrack.jetbrains.com/issue/KTOR-4225
//            validateResponse { response ->
//                val error: GithubQueryErrorResponse = response.body()
//                if (error.errors.isNotEmpty()) {
//                    throw GithubQueryErrorException(error)
//                }
//            }
        }
    }

    private val url = "https://api.github.com/graphql"

    override suspend fun <T> postQuery(query: String, typeInfo: TypeInfo): GithubQueryResponse<T> {
        return client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(QueryRequest(query))
        }.body(typeInfo) as GithubQueryResponse<T>
    }
}

@Serializable
data class QueryRequest(val query: String)