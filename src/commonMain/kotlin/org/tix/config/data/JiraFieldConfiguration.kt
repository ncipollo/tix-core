package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.config.data.dynamic.DynamicProperty

@Serializable
data class JiraFieldConfiguration(
    val default: Map<String, @Contextual DynamicProperty> = mapOf(),
    val epic: Map<String, @Contextual DynamicProperty> = mapOf(),
    val issue: Map<String, @Contextual DynamicProperty> = mapOf(),
    val task: Map<String, @Contextual DynamicProperty> = mapOf(),
)