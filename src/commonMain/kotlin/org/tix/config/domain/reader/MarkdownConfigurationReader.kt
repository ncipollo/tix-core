package org.tix.config.domain.reader

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.parser.MarkdownParser
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.feature.plan.domain.parse.configparser.ConfigLanguageDetector
import org.tix.feature.plan.domain.parse.defaultMarkdownParser
import org.tix.feature.plan.domain.parse.nodeparser.CodeFenceSegmentCreator

class MarkdownConfigurationReader(private val markdownParser: MarkdownParser = defaultMarkdownParser()) {
    private val configParser = MarkdownConfigParser()
    private val headerTypes = listOf(
        MarkdownElementTypes.ATX_1.name,
        MarkdownElementTypes.ATX_2.name,
        MarkdownElementTypes.ATX_3.name,
        MarkdownElementTypes.ATX_4.name,
        MarkdownElementTypes.ATX_5.name,
        MarkdownElementTypes.ATX_6.name
    )

    fun configFromMarkdown(markdown: String?): RawTixConfiguration? {
        if (markdown == null) {
            return null
        }
        val node = markdownTree(markdown)
        val preambleNodes = node.children.takeWhile { !headerTypes.contains(it.type.name) }
        return preambleNodes.firstCodeFence()
            ?.let {codeFence ->
                val segment = CodeFenceSegmentCreator.createCodeSegment(codeFence, markdown)
                val configType = ConfigLanguageDetector.detect(segment)
                configParser.parse(segment.code, configType)
            }
    }

    private fun markdownTree(markdown: String) = markdownParser.buildMarkdownTreeFromString(markdown)

    private fun List<ASTNode>.firstCodeFence() =
        firstOrNull { it.type.name == MarkdownElementTypes.CODE_FENCE.name }
}