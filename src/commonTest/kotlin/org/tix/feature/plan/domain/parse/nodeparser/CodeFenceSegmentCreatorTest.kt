package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.tix.feature.plan.domain.parse.defaultMarkdownParser
import org.tix.ticket.body.CodeBlockSegment
import kotlin.test.Test
import kotlin.test.expect

class CodeFenceSegmentCreatorTest {
    private val markdownParser = defaultMarkdownParser()

    @Test
    fun parse_withLanguage() {
        val markdown = """
            ```code
            code goes here
            *next line*
            ```
        """.trimIndent()
        val node = codeFenceNode(markdown)
        expect(CodeBlockSegment(code = "\ncode goes here\n*next line*\n", language = "code")) {
            CodeFenceSegmentCreator.createCodeSegment(node, markdown)
        }

    }

    @Test
    fun parse_withoutLanguage() {
        val markdown = """
            ```
            code goes here
            *next line*
            ```
        """.trimIndent()
        val node = codeFenceNode(markdown)
        expect(CodeBlockSegment(code = "\ncode goes here\n*next line*\n", language = "")) {
            CodeFenceSegmentCreator.createCodeSegment(node, markdown)
        }

    }

    private fun codeFenceNode(markdown: String) =
        markdownParser.buildMarkdownTreeFromString(markdown)
            .children
            .first { it.type == MarkdownElementTypes.CODE_FENCE }
}