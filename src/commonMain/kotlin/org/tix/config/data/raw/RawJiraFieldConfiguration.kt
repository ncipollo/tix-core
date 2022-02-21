package org.tix.config.data.raw

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement

@Serializable
data class RawJiraFieldConfiguration(
    val default: Map<String, @Contextual DynamicElement> = mapOf(),
    val epic: Map<String, @Contextual DynamicElement> = mapOf(),
    val issue: Map<String, @Contextual DynamicElement> = mapOf(),
    val task: Map<String, @Contextual DynamicElement> = mapOf(),
)