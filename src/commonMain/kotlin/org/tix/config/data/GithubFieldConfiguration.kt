package org.tix.config.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement

@Serializable
data class GithubFieldConfiguration(
    val default: Map<String, @Contextual DynamicElement> = mapOf(),
    val project: Map<String, @Contextual DynamicElement> = mapOf(),
    val issue: Map<String, @Contextual DynamicElement> = mapOf()
) : FieldConfiguration {
    override fun forLevel(level: Int): Map<String, DynamicElement> {
        val overlay = when (level) {
            0 -> project
            1 -> issue
            else -> emptyMap()
        }
        return default + overlay
    }
}