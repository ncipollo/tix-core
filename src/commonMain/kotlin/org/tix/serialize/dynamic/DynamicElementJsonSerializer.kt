package org.tix.serialize.dynamic

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.*
import org.tix.ext.isWholeNumber

object DynamicElementJsonSerializer : DynamicElementSerializer {
    override fun deserialize(decoder: Decoder): DynamicElement {
        val jsonDecoder = decoder as JsonDecoder
        val jsonElement = jsonDecoder.decodeJsonElement()
        val value = decodeJsonElement(jsonElement)

        return DynamicElement(value)
    }

    private fun decodeJsonElement(jsonElement: JsonElement): Any? =
        when (jsonElement) {
            is JsonArray -> decodeJsonArray(jsonElement)
            is JsonObject -> decodeJsonObject(jsonElement)
            is JsonPrimitive -> decodeJsonPrimitive(jsonElement)
        }

    private fun decodeJsonArray(array: JsonArray) = array.map { decodeJsonElement(it) }

    private fun decodeJsonPrimitive(primitive: JsonPrimitive): Any? {
        if (primitive.isString) return primitive.content

        val boolValue = primitive.booleanOrNull
        if (boolValue != null) return boolValue

        val doubleValue = primitive.doubleOrNull
        if (doubleValue != null) {
            return if (doubleValue.isWholeNumber()) {
                doubleValue.toLong()
            } else {
                doubleValue
            }
        }

        return null
    }

    private fun decodeJsonObject(jsonObject: JsonObject) =
        jsonObject.mapValues { (_, element) -> decodeJsonElement(element) }
}