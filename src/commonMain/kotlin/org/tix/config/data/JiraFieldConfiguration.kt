package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement

@Serializable
data class JiraFieldConfiguration(
    val default: Map<String, @Contextual DynamicElement> = mapOf(),
    val epic: Map<String, @Contextual DynamicElement> = mapOf(),
    val issue: Map<String, @Contextual DynamicElement> = mapOf(),
    val task: Map<String, @Contextual DynamicElement> = mapOf(),
)