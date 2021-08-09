package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement

@Serializable
data class GithubFieldConfiguration(
    val default: Map<String, @Contextual DynamicElement> = mapOf(),
    val project: Map<String, @Contextual DynamicElement> = mapOf(),
    val issue: Map<String, @Contextual DynamicElement> = mapOf()
)