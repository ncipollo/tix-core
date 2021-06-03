package org.tix.config.serialize

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.mamoe.yamlkt.Yaml
import org.tix.config.data.dynamic.DynamicPropertyJsonSerializer
import org.tix.config.data.dynamic.DynamicPropertyYamlSerializer

object ConfigurationSerializers {
    fun json() = Json {
        encodeDefaults = false
        prettyPrint = true
        serializersModule = SerializersModule {
            contextual(DynamicPropertyJsonSerializer)
        }
    }

    fun yaml() = Yaml {
        encodeDefaultValues = false
        serializersModule = SerializersModule {
            contextual(DynamicPropertyYamlSerializer)
        }
    }
}