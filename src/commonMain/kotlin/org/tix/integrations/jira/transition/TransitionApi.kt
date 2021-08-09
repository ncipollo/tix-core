package org.tix.integrations.jira.transition

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tix.net.BaseUrl
import org.tix.serialize.emptyJson

class TransitionApi(private val baseUrl: BaseUrl, private val client: HttpClient) {
    suspend fun transitions(issueId: String): List<Transition> {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}/transitions")
        val result = client.get<TransitionResult>(url) {
            parameter("expand", "transitions.fields")
        }
        return result.transitions
    }

    suspend fun transitionIssue(
        issueId: String,
        transitionId: String,
        fields: JsonObject = emptyJson(),
        update: JsonObject = emptyJson()
    ) {
        val url = baseUrl.withPath("rest/api/2/issue/${issueId}/transitions")
        client.post<Unit>(url) {
            contentType(ContentType.Application.Json)
            body = IssueTransitionBody(
                transition = IssueTransition(transitionId),
                fields = fields,
                update = update
            )
        }
    }

    @Serializable
    data class TransitionResult(val transitions: List<Transition>)

    @Serializable
    data class IssueTransition(val id: String)

    @Serializable
    data class IssueTransitionBody(val transition: IssueTransition, val fields: JsonObject, val update: JsonObject)
}