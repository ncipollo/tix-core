package org.tix.feature.plan.parse.nodeparser

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.MarkdownTokenTypes
import kotlin.test.*

class ParserArgumentsTest {
    private val initialArguments = """
        # Section 1
        Body 1
        # Section 2
        Body 2
    """.trimIndent().toParserArguments()

    @Test
    fun childArguments_noChildren() {
        expect(null) {
            initialArguments.childArguments!!.childArguments
        }
    }

    @Test
    fun childArguments_withChildren() {
        expect(initialArguments.copy(nodes = initialArguments.currentNode.children, nodeIndex = 0)) {
            initialArguments.childArguments
        }
    }

    @Test
    fun currentNode() {
        expect(MarkdownElementTypes.ATX_1.toString()) { initialArguments.currentNode.type.toString() }
    }

    @Test
    fun previousNode_returnsNullWhenNoPreviousNode() {
        expect(null) { initialArguments.previousNode }
    }

    @Test
    fun nextArgsFromResult() {
        val nextArguments = initialArguments.nextArgsFromResult(ParserResult(nextIndex = 2))
        assertEquals(initialArguments.copy(nodeIndex = 2), nextArguments)
    }

    @Test
    fun previousNode_returnsPreviousNode() {
        val arguments = initialArguments.copy(nodeIndex = 1)
        expect(MarkdownElementTypes.ATX_1.toString()) { arguments.previousNode?.type.toString() }
    }

    @Test
    fun nextNode_returnsNullWhenNoNextNode() {
        val arguments = initialArguments.copy(nodeIndex = 8)
        expect(null) { arguments.nextNode }
    }

    @Test
    fun nextNode_returnsNextNode() {
        expect(MarkdownTokenTypes.EOL.toString()) { initialArguments.nextNode?.type.toString() }
    }

    @Test
    fun resultsFromArgs() {
        val arguments = initialArguments.copy(nodeIndex = 1)
        expect(ParserResult(nextIndex = 3)) { arguments.resultsFromArgs(2) }
    }

    @Test
    fun shouldContinueParsing_returnsFalseWhenNoMoreNodesExist() {
        val arguments = initialArguments.copy(nodeIndex = 8)
        assertFalse { arguments.shouldContinueParsing }
    }

    @Test
    fun shouldContinueParsing_returnsTrueWhenMoreNodesExist() {
        assertTrue { initialArguments.shouldContinueParsing }
    }

    @Test
    fun textInCurrentNode() {
        expect("# Section 1") { initialArguments.textInCurrentNode() }
    }
}