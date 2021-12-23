package org.tix.feature.plan.domain.parse.nodeparser

import kotlinx.serialization.SerializationException
import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.domain.parse.fieldparser.FieldLanguage
import org.tix.feature.plan.domain.parse.fieldparser.FieldLanguageDetector
import org.tix.feature.plan.domain.parse.fieldparser.FieldParser
import org.tix.feature.plan.domain.parse.parseError
import org.tix.ticket.body.CodeBlockSegment

internal class CodeFenceParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val segment = CodeBlockSegment(code = code(arguments), language = lang(arguments))

        val fieldLanguage = FieldLanguageDetector.detect(segment)
        if (fieldLanguage == FieldLanguage.NO_FIELDS) {
            arguments.state.addBodySegments(segment)
        } else {
            try {
                val fields = FieldParser.parse(segment.code, fieldLanguage)
                arguments.state.addFields(fields)
            } catch (e: SerializationException) {
                parseError("Failed to parse tix fields.\nSerialization Error: $e", arguments)
            }
        }

        return arguments.resultsFromArgs()
    }

    private fun code(arguments: ParserArguments) =
        arguments.currentNode.children
            .joinToString(separator = "") { mapChildNode(it, arguments) }

    private fun mapChildNode(node: ASTNode, arguments: ParserArguments) =
        when(node.type.name) {
            MarkdownTokenTypes.CODE_FENCE_CONTENT.name -> node.getTextInNode(arguments.markdownText).toString()
            MarkdownTokenTypes.EOL.name -> "\n"
            else -> ""
        }

    private fun lang(arguments: ParserArguments) =
        arguments.currentNode
            .findChildOfType(MarkdownTokenTypes.FENCE_LANG)
            ?.getTextInNode(arguments.markdownText)
            ?.toString() ?: ""
}