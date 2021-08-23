package org.tix.serialize

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.mamoe.yamlkt.Yaml
import org.tix.serialize.dynamic.DynamicElement
import org.tix.serialize.dynamic.DynamicElementJsonSerializer
import org.tix.serialize.dynamic.DynamicElementYamlSerializer

object TixSerializers {
    fun json() = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = SerializersModule {
            contextual(DynamicElementJsonSerializer)
        }
    }

    fun yaml() = Yaml {
        encodeDefaultValues = false
        serializersModule = SerializersModule {
            contextual(DynamicElementYamlSerializer)
        }
    }
}

fun Json.decodeDynamicElement(text: String) = decodeFromString(DynamicElementJsonSerializer, text)

fun Json.encodeDynamicElement(element: DynamicElement) = encodeToString(DynamicElementJsonSerializer, element)

fun Yaml.decodeDynamicElement(text: String) = decodeFromString(DynamicElementYamlSerializer, text)

fun Yaml.encodeDynamicElement(element: DynamicElement) = encodeToString(DynamicElementYamlSerializer, element)