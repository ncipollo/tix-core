package org.tix.config.data.raw

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic

@Serializable
data class RawTixConfiguration(
    @Contextual val include: DynamicElement = emptyDynamic(),
    val github: RawGithubConfiguration? = null,
    val jira: RawJiraConfiguration? = null,
    val variables: Map<String, String> = emptyMap(),
    @SerialName("variable_token") val variableToken: String? = null
)