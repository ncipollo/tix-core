package org.tix.config.data.dynamic

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class DynamicProperty(
    val boolean: Boolean? = null,
    @Contextual val number: Number? = null,
    val string: String? = null,
    val stringList: List<String>? = null
) {
    fun asBoolean() =
        when {
            boolean != null -> boolean
            number != null -> number.toInt() != 0
            else -> false
        }

    fun asDouble() = number?.toDouble() ?: 0

    fun asLong() = number?.toLong() ?: 0

    fun asString() =
        when {
            string != null -> string
            !stringList.isNullOrEmpty() -> stringList.first()
            boolean != null -> boolean.toString()
            number != null -> number.toString()
            else -> ""
        }

    fun asStringList() =
        when {
            stringList != null -> stringList
            else -> string?.let { listOf(it) } ?: emptyList()
        }
}

inline fun emptyDynamic() = DynamicProperty()