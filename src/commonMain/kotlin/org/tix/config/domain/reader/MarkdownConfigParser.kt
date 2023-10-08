package org.tix.config.domain.reader

import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.error.TixError
import org.tix.feature.plan.domain.parse.configparser.ConfigLanguage
import org.tix.serialize.TixSerializers

class MarkdownConfigParser {
    private val json = TixSerializers.json()
    private val yaml = TixSerializers.yaml()

    fun parse(code: String, language: ConfigLanguage) =
        when (language) {
            ConfigLanguage.JSON -> parseJson(code)
            ConfigLanguage.NO_CONFIG -> null
            ConfigLanguage.YAML -> parseYaml(code)
        }

    private fun parseJson(code: String) = rethrowErrorsWithMoreContext {
        json.decodeFromString<RawTixConfiguration>(code)
    }

    private fun parseYaml(code: String) = rethrowErrorsWithMoreContext {
        yaml.decodeFromString<RawTixConfiguration>(code)
    }

    private fun rethrowErrorsWithMoreContext(decodeBlock: () -> RawTixConfiguration) =
        try {
            decodeBlock()
        } catch (ex: SerializationException) {
            throw TixError(message = "Failed to parse configuration in markdown file", cause = ex)
        }
}