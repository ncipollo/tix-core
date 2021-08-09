package org.tix.integrations.shared.custom

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.emptyDynamic

@Serializable
data class CustomRequest(
    val method: CustomRequestMethod = CustomRequestMethod.GET,
    val path: String = "",
    val parameters: Map<String, String> = emptyMap(),
    @Contextual val body: DynamicElement = emptyDynamic()
)
