package org.tix.config.data

import kotlinx.serialization.Contextual
import org.tix.serialize.dynamic.DynamicElement

interface FieldConfiguration {
    fun forLevel(level: Int):  Map<String, @Contextual DynamicElement>
}