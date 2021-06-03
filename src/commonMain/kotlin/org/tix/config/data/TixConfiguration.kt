package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.data.dynamic.emptyDynamic

@Serializable
data class TixConfiguration(
    @Contextual val include: DynamicProperty = emptyDynamic(),
    val github: GithubConfiguration = GithubConfiguration(),
    val jira: JiraConfiguration = JiraConfiguration(),
    val variables: Map<String, String> = emptyMap()
)