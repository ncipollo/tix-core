package org.tix.config.data.raw

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement

@Serializable
data class RawGithubFieldConfiguration(
    val default: Map<String, @Contextual DynamicElement> = mapOf(),
    val project: Map<String, @Contextual DynamicElement> = mapOf(),
    val issue: Map<String, @Contextual DynamicElement> = mapOf()
)