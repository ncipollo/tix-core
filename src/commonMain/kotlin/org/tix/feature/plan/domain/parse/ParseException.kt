package org.tix.feature.plan.domain.parse

import org.intellij.markdown.ast.ASTNode
import org.tix.feature.plan.domain.parse.nodeparser.ParserArguments

class ParseException(message: String, node: ASTNode, markdownText: String) :
    RuntimeException(parserMessage(message, node, markdownText))

internal fun parseError(message: Any, node: ASTNode, markdownText: String): Nothing =
    throw ParseException(message.toString(), node, markdownText)

internal fun parseError(message: Any, arguments: ParserArguments): Nothing =
    parseError(message, arguments.currentNode, arguments.markdownText)

private fun parserMessage(message: String, node: ASTNode, markdownText: String) =
    "Markdown Parser  (L${node.lineNumber(markdownText)}): $message"