package org.tix.feature.plan.domain.parse.fieldparser

import org.tix.serialize.TixSerializers
import org.tix.serialize.decodeDynamicElement
import org.tix.serialize.dynamic.emptyDynamic

object FieldParser {
    private val json = TixSerializers.json()
    private val yaml = TixSerializers.yaml()

    fun parse(code: String, language: FieldLanguage) =
        when (language) {
            FieldLanguage.JSON -> parseJson(code)
            FieldLanguage.NO_FIELDS -> emptyDynamic()
            FieldLanguage.YAML -> parseYaml(code)
        }

    private fun parseJson(code: String) = json.decodeDynamicElement(code)

    private fun parseYaml(code: String) = yaml.decodeDynamicElement(code)
}