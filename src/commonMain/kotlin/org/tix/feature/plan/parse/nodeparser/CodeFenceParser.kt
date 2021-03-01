package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.model.ticket.body.CodeBlockSegment

internal class CodeFenceParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val code = code(arguments)
        val lang = lang(arguments)
        arguments.state.addBodySegments(CodeBlockSegment(code = code, language = lang))
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