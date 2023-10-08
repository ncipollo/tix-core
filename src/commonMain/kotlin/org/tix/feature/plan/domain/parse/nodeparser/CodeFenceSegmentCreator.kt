package org.tix.feature.plan.domain.parse.nodeparser

import org.intellij.markdown.MarkdownTokenTypes
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.findChildOfType
import org.intellij.markdown.ast.getTextInNode
import org.tix.ticket.body.CodeBlockSegment

object CodeFenceSegmentCreator {
    fun createCodeSegment(node: ASTNode, markdownText: String) =
        CodeBlockSegment(code = code(node, markdownText), language = lang(node, markdownText))

    private fun code(node: ASTNode, markdownText: String) =
        node.children
            .joinToString(separator = "") { mapChildNode(it, markdownText) }

    private fun mapChildNode(node: ASTNode, markdownText: String) =
        when (node.type.name) {
            MarkdownTokenTypes.CODE_FENCE_CONTENT.name -> node.getTextInNode(markdownText).toString()
            MarkdownTokenTypes.EOL.name -> "\n"
            else -> ""
        }

    private fun lang(node: ASTNode, markdownText: String) =
        node
            .findChildOfType(MarkdownTokenTypes.FENCE_LANG)
            ?.getTextInNode(markdownText)
            ?.toString() ?: ""
}