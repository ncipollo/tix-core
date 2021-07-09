package org.tix.config.data.raw

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.data.dynamic.emptyDynamic

@Serializable
data class RawTixConfiguration(
    @Contextual val include: DynamicProperty = emptyDynamic(),
    val github: RawGithubConfiguration = RawGithubConfiguration(),
    val jira: RawJiraConfiguration = RawJiraConfiguration(),
    val variables: Map<String, String> = emptyMap()
)