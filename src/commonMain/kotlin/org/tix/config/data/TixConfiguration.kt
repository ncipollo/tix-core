package org.tix.config.data

import kotlinx.serialization.Contextual
import org.tix.serialize.dynamic.DynamicElement

data class TixConfiguration(
    @Contextual val include: DynamicElement,
    val github: GithubConfiguration?,
    val jira: JiraConfiguration?,
    val matrix: Map<String, List<MatrixEntryConfiguration>>,
    val variables: Map<String, String>,
    val variableToken: String
)