package org.tix.serialize.dynamic

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Encoder
import org.tix.ext.isWholeNumber

interface DynamicElementSerializer : KSerializer<DynamicElement> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("org.tix.serialize.dynamic.DynamicElement")

    @ExperimentalSerializationApi
    override fun serialize(encoder: Encoder, value: DynamicElement) {
        when (val elementValue = value.value) {
            is Boolean -> encoder.encodeBoolean(elementValue)
            is Number -> encodeNumber(encoder, elementValue)
            is String -> encoder.encodeString(elementValue)
            is List<*> -> encodeList(encoder, elementValue)
            is Map<*,*> -> encodeMap(encoder, elementValue)
            else -> encoder.encodeNull()
        }
    }

    private fun encodeNumber(encoder: Encoder, number: Number) =
        if (number.isWholeNumber()) {
            encoder.encodeLong(number.toLong())
        } else {
            encoder.encodeDouble(number.toDouble())
        }

    private fun encodeList(encoder: Encoder, anyList: List<*>) {
        val dynamicList = anyList.map { DynamicElement(it) }
        encoder.encodeSerializableValue(ListSerializer(this), dynamicList)
    }

    private fun encodeMap(encoder: Encoder, anyMap: Map<*, *>) {
        val dynamicMap = anyMap.map { (key, value) -> key.toString() to DynamicElement(value) }.toMap()
        encoder.encodeSerializableValue(
            MapSerializer(String.serializer(), this),
            dynamicMap
        )
    }
}

