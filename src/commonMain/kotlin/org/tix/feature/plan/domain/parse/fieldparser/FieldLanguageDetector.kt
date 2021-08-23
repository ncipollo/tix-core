package org.tix.feature.plan.domain.parse.fieldparser

import org.tix.model.ticket.body.CodeBlockSegment

object FieldLanguageDetector {
    fun detect(block: CodeBlockSegment) =
        when(block.language) {
            "tix" -> detectByCode(block)
            "tix_json" -> FieldLanguage.JSON
            "tix_yaml", "tix_yml" -> FieldLanguage.YAML
            else -> FieldLanguage.NO_FIELDS
        }

    private fun detectByCode(codeBlock: CodeBlockSegment) =
        if(firstStatement(codeBlock).startsWith("{") ) {
            FieldLanguage.JSON
        } else {
            FieldLanguage.YAML
        }

    private fun firstStatement(codeBlock: CodeBlockSegment) =
        codeBlock.code
            .split('\n')
            .firstOrNull { it.isNotBlank() }
            ?.trim() ?: ""
}