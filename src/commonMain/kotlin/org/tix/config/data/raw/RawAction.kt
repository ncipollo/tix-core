package org.tix.config.data.raw

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic

@Serializable
data class RawAction(
    val type: String = "",
    val label: String = type,
    @Contextual val arguments: DynamicElement = emptyDynamic()
)
