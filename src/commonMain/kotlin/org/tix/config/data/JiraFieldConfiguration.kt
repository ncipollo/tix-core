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
) : FieldConfiguration {
    override fun forLevel(level: Int): Map<String, DynamicElement> {
        val overlay = when (level) {
            0 -> epic
            1 -> issue
            2 -> task
            else -> emptyMap()
        }
        return default + overlay
    }
}