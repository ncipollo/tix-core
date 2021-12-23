package org.tix.feature.plan.domain.parse.fieldparser

import org.tix.ticket.body.CodeBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class FieldLanguageDetectorTest {
    @Test
    fun detect_whenLanguageIsNotTix() {
        val block = CodeBlockSegment(language = "kotlin")
        expect(FieldLanguage.NO_FIELDS) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixJson() {
        val block = CodeBlockSegment(language = "tix_json")
        expect(FieldLanguage.JSON) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyIsEmpty() {
        val block = CodeBlockSegment(language = "tix")
        expect(FieldLanguage.YAML) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyDoesNotStartWithCurley() {
        val code = """
            firstItem: foo
            {}
        """.trimIndent()
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(FieldLanguage.YAML) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyStartsWithCurley_noLeadingSpace() {
        val code = "{"
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(FieldLanguage.JSON) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTix_bodyStartsWithCurley_whiteSpaceAndLeadingSpace() {
        val code = """
            
            {
              "foo": "bar"
            }
        """.trimIndent()
        val block = CodeBlockSegment(code = code, language = "tix")
        expect(FieldLanguage.JSON) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixYaml() {
        val block = CodeBlockSegment(language = "tix_yml")
        expect(FieldLanguage.YAML) { FieldLanguageDetector.detect(block) }
    }

    @Test
    fun detect_whenLanguageIsTixYml() {
        val block = CodeBlockSegment(language = "tix_yml")
        expect(FieldLanguage.YAML) { FieldLanguageDetector.detect(block) }
    }
}