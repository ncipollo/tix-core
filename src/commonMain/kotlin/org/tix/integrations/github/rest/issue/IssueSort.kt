package org.tix.integrations.github.rest.issue

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class IssueSort {
    @SerialName("created") CREATED,
    @SerialName("updated") UPDATED,
    @SerialName("comments") COMMENTS
}