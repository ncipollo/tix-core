package org.tix.config.data

data class GithubFieldConfiguration(
    val default: Map<String, Any?> = mapOf(),
    val project: Map<String, Any?> = mapOf(),
    val issue: Map<String, Any?> = mapOf()
) : FieldConfiguration {
    override fun forLevel(level: Int): Map<String, Any?> {
        val overlay = when (level) {
            0 -> project
            1 -> issue
            else -> emptyMap()
        }
        return default + overlay
    }
}