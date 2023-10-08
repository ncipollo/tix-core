package org.tix.feature.plan.domain.parse.configparser

import org.tix.ticket.body.CodeBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class ConfigLanguageDetectorTest {
    @Test
    fun detect_whenLanguageIsNotTix() {
        val block = CodeBlockSegment(language = "kotlin")
        expect(ConfigLanguage.NO_CONFIG) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsConfigTixJson() {
        val block = CodeBlockSegment(language = "tix_config_json")
        expect(ConfigLanguage.JSON) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixJson() {
        val block = CodeBlockSegment(language = "tix_json")
        expect(ConfigLanguage.JSON) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyIsEmpty() {
        val block = CodeBlockSegment(language = "tix")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyDoesNotStartWithCurley() {
        val code = """
            firstItem: foo
            {}
        """.trimIndent()
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyStartsWithCurley_noLeadingSpace() {
        val code = "{"
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(ConfigLanguage.JSON) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixConfig_bodyStartsWithCurley_noLeadingSpace() {
        val code = "{"
        val block = CodeBlockSegment(code = code, language = "tix_config")
        expect(ConfigLanguage.JSON) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyStartsWithCurley_whiteSpaceAndLeadingSpace() {
        val code = """
            
            {
              "foo": "bar"
            }
        """.trimIndent()
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(ConfigLanguage.JSON) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixConfigYaml() {
        val block = CodeBlockSegment(language = "tix_config_yaml")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixConfigYml() {
        val block = CodeBlockSegment(language = "tix_config_yml")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixYaml() {
        val block = CodeBlockSegment(language = "tix_yml")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixYml() {
        val block = CodeBlockSegment(language = "tix_yml")
        expect(ConfigLanguage.YAML) { ConfigLanguageDetector.detect(block) }
    }
}