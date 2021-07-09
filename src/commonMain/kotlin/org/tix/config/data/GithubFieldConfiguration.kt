package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty

@Serializable
data class GithubFieldConfiguration(
    val default: Map<String, @Contextual DynamicProperty> = mapOf(),
    val project: Map<String, @Contextual DynamicProperty> = mapOf(),
    val issue: Map<String, @Contextual DynamicProperty> = mapOf()
)