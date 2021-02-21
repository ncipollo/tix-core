package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.ast.ASTNode
import org.tix.feature.plan.parse.state.ParserState

internal data class ParserArguments(
    val markdownText: String = "",
    private val nodes: List<ASTNode> = emptyList(),
    private val nodeIndex: Int = 0,
    val state: ParserState = ParserState()
) {
    val currentNode by lazy { nodes[nodeIndex] }
    val nextNode by lazy { nodes.getOrNull(nodeIndex + 1) }
    val previousNode by lazy { nodes.getOrNull(nodeIndex - 1) }
    val shouldContinueParsing get() = nodeIndex < nodes.size

    fun nextArgsFromResult(result: ParserResult) = copy(nodeIndex = result.nextIndex)

    fun resultsFromArgs(indexIncrement: Int) = ParserResult(nextIndex = nodeIndex + indexIncrement)
}

internal fun parserArguments(markdownText: String, nodes: List<ASTNode>) =
    ParserArguments(
        markdownText = markdownText,
        nodes = nodes
    )