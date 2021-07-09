package org.tix.config.data

import kotlinx.serialization.Contextual
import org.tix.config.data.dynamic.DynamicProperty

data class TixConfiguration(
    @Contextual val include: DynamicProperty,
    val github: GithubConfiguration,
    val jira: JiraConfiguration,
    val variables: Map<String, String>
)