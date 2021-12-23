package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.tix.ticket.body.CodeBlockSegment

internal class CodeBlockParser: NodeParser {
    override fun parse(arguments: ParserArguments): ParserResult {
        val code = "\n${code(arguments)}\n"
        arguments.state.addBodySegments(CodeBlockSegment(code = code))
        return arguments.resultsFromArgs()
    }

    private fun code(arguments: ParserArguments) =
        arguments.currentNode.children
            .joinToString(separator = "") { mapChildNode(it, arguments) }

    private fun mapChildNode(node: ASTNode, arguments: ParserArguments) =
        when(node.type.name) {
            MarkdownTokenTypes.CODE_LINE.name -> node.getTextInNode(arguments.markdownText).toString().trimIndent()
            MarkdownTokenTypes.EOL.name -> "\n"
            else -> ""
        }
}