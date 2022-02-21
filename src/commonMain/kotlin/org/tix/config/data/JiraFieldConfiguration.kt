package org.tix.config.data

data class JiraFieldConfiguration(
    val default: Map<String, Any?> = mapOf(),
    val epic: Map<String, Any?> = mapOf(),
    val issue: Map<String, Any?> = mapOf(),
    val task: Map<String, Any?> = mapOf(),
) : FieldConfiguration {
    override fun forLevel(level: Int): Map<String, Any?> {
        val overlay = when (level) {
            0 -> epic
            1 -> issue
            2 -> task
            else -> emptyMap()
        }
        return default + overlay
    }
}