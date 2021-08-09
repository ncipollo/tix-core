package org.tix.serialize.dynamic

import kotlinx.serialization.encoding.Decoder
import net.mamoe.yamlkt.YamlDynamicSerializer

object DynamicElementYamlSerializer : DynamicElementSerializer {
    override fun deserialize(decoder: Decoder): DynamicElement {
        val value = YamlDynamicSerializer.deserialize(decoder)
        return DynamicElement(value)
    }
}