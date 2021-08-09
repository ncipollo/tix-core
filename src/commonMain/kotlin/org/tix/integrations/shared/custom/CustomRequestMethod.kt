package org.tix.integrations.shared.custom

import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CustomRequestMethod {
    @SerialName("delete")
    DELETE,

    @SerialName("get")
    GET,

    @SerialName("post")
    POST,

    @SerialName("put")
    PUT;

    fun toHttpMethod() =
        when (this) {
            DELETE -> HttpMethod.Delete
            GET -> HttpMethod.Get
            POST -> HttpMethod.Post
            PUT -> HttpMethod.Put
        }
}