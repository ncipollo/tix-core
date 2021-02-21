package org.tix.feature.plan.parse

import org.intellij.markdown.ast.ASTNode

class ParseException(message: String, node: ASTNode, markdownText: String) :
    RuntimeException(parserMessage(message, node, markdownText))

fun parseError(message: Any, node: ASTNode, markdownText: String): Nothing =
    throw ParseException(message.toString(), node, markdownText)

private fun parserMessage(message: String, node: ASTNode, markdownText: String) =
    "Markdown Parser  (L${node.lineNumber(markdownText)}): $message"