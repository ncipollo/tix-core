package org.tix.feature.plan.domain.parse.fieldparser

import org.tix.feature.plan.domain.parse.configparser.ConfigLanguage
import org.tix.serialize.TixSerializers
import org.tix.serialize.decodeDynamicElement
import org.tix.serialize.dynamic.emptyDynamic

object FieldParser {
    private val json = TixSerializers.json()
    private val yaml = TixSerializers.yaml()

    fun parse(code: String, language: ConfigLanguage) =
        when (language) {
            ConfigLanguage.JSON -> parseJson(code)
            ConfigLanguage.NO_CONFIG -> emptyDynamic()
            ConfigLanguage.YAML -> parseYaml(code)
        }

    private fun parseJson(code: String) = json.decodeDynamicElement(code)

    private fun parseYaml(code: String) = yaml.decodeDynamicElement(code)
}