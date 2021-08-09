package org.tix.serialize.dynamic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class DynamicElement(@Contextual val value: Any? = null) {
    fun asBoolean() =
        when (value) {
            is Boolean -> value
            is Number -> value.toLong() > 0L
            else -> false
        }

    fun asDouble() = asNumber().toDouble()

    fun asInt() = asNumber().toInt()

    fun asLong() = asNumber().toLong()

    private fun asNumber() = value as? Number ?: 0

    fun asString() =
        when (value) {
            is String -> value
            else -> value?.toString() ?: ""
        }

    fun asList(): List<*> =
        when (value) {
            is List<*> -> value
            else -> listOfNotNull(value)
        }

    fun asMap(defaultKey: String? = null) =
        when {
            value is Map<*, *> -> value
            defaultKey != null && value != null -> mapOf(defaultKey to value)
            else -> emptyMap()
        }

    fun isNotEmpty() = value != null
}

inline fun emptyDynamic() = DynamicElement()