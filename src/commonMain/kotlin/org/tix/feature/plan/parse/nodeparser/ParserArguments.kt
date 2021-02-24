package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.getTextInNode
import org.tix.feature.plan.parse.state.ParserState

internal data class ParserArguments(
    val markdownText: String = "",
    private val nodes: List<ASTNode> = emptyList(),
    private val nodeIndex: Int = 0,
    val state: ParserState = ParserState()
) {
    val childArguments by lazy {
        currentNode.children
            .takeIf { it.isNotEmpty() }
            ?.let { copy(nodes = it, nodeIndex = 0) }
    }
    val currentNode by lazy { nodes[nodeIndex] }
    val nextNode by lazy { nodes.getOrNull(nodeIndex + 1) }
    val previousNode by lazy { nodes.getOrNull(nodeIndex - 1) }
    val shouldContinueParsing get() = nodeIndex < nodes.size

    fun nextArgsFromResult(result: ParserResult) = copy(nodeIndex = result.nextIndex)

    fun resultsFromArgs(indexIncrement: Int = 1) = ParserResult(nextIndex = nodeIndex + indexIncrement)

    fun textInCurrentNode() = currentNode.getTextInNode(markdownText).toString()
}

internal fun parserArguments(markdownText: String, nodes: List<ASTNode>) =
    ParserArguments(
        markdownText = markdownText,
        nodes = nodes
    )