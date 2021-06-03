package org.tix.config.data.dynamic

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import net.mamoe.yamlkt.YamlDynamicSerializer
import org.tix.ext.isWholeNumber

interface DynamicPropertySerializer : KSerializer<DynamicProperty> {
    override val descriptor get() = buildClassSerialDescriptor("org.tix.config.data.dynamic.FieldValue")

    override fun serialize(encoder: Encoder, value: DynamicProperty) {
        when {
            value.boolean != null -> encoder.encodeBoolean(value.boolean)
            value.number != null -> {
                if (value.number.isWholeNumber()) {
                    encoder.encodeLong(value.number.toLong())
                } else {
                    encoder.encodeDouble(value.number.toDouble())
                }
            }
            value.string != null -> encoder.encodeString(value.string)
            value.stringList != null -> encoder.encodeSerializableValue(
                ListSerializer(String.serializer()),
                value.stringList
            )
        }
    }
}

object DynamicPropertyYamlSerializer : DynamicPropertySerializer {
    override fun deserialize(decoder: Decoder): DynamicProperty {
        return when (val value = YamlDynamicSerializer.deserialize(decoder)) {
            is Boolean -> DynamicProperty(boolean = value)
            is Number -> DynamicProperty(number = value)
            is List<*> -> DynamicProperty(stringList = value.mapNotNull { it?.toString() })
            is String -> DynamicProperty(string = value)
            else -> error("Unsupported value type: ${value::class.simpleName}")
        }
    }
}

object DynamicPropertyJsonSerializer : DynamicPropertySerializer {
    override fun deserialize(decoder: Decoder): DynamicProperty {
        val jsonDecoder = decoder as JsonDecoder
        return when (val jsonElement = jsonDecoder.decodeJsonElement()) {
            is JsonPrimitive -> decodePrimitive(jsonElement)
            is JsonArray -> decodeJsonArray(jsonElement)
            else -> error("Unsupported json type: ${jsonElement::class.simpleName}")
        }
    }

    private fun decodePrimitive(primitive: JsonPrimitive): DynamicProperty {
        if (primitive.isString) return DynamicProperty(string = primitive.content)

        val doubleValue = primitive.doubleOrNull
        val number = if (doubleValue?.isWholeNumber() == true) {
            doubleValue.toLong()
        } else {
            doubleValue
        }
        return DynamicProperty(
            boolean = primitive.booleanOrNull,
            number = number,
        )
    }

    private fun decodeJsonArray(array: JsonArray): DynamicProperty {
        val stringList = array.filterIsInstance<JsonPrimitive>().map { it.content }
        return DynamicProperty(stringList = stringList)
    }
}