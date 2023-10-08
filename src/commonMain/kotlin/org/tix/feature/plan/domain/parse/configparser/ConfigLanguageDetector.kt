package org.tix.feature.plan.domain.parse.configparser

import org.tix.ticket.body.CodeBlockSegment

object ConfigLanguageDetector {
    fun detect(block: CodeBlockSegment) =
        when(block.language) {
            "tix", "tix_config" -> detectByCode(block)
            "tix_json", "tix_config_json" -> ConfigLanguage.JSON
            "tix_yaml", "tix_yml", "tix_config_yaml", "tix_config_yml" -> ConfigLanguage.YAML
            else -> ConfigLanguage.NO_CONFIG
        }

    private fun detectByCode(codeBlock: CodeBlockSegment) =
        if(firstStatement(codeBlock).startsWith("{") ) {
            ConfigLanguage.JSON
        } else {
            ConfigLanguage.YAML
        }

    private fun firstStatement(codeBlock: CodeBlockSegment) =
        codeBlock.code
            .split('\n')
            .firstOrNull { it.isNotBlank() }
            ?.trim() ?: ""
}