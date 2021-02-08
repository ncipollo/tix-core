package org.tix

import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.Visitor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import kotlin.test.Test
import kotlin.test.expect

class SanityTest {
    val markdown =
        """
            # Section 1
            This is a section.
            Here's a list:
            - item 1
            - item 2
            - item *3*
        """.trimIndent()
    @Test
    fun testSanity() {
        expect(true) { true }
    }

    @Test
    fun markdownTest() {
        val src = "Some *Markdown*"
        val flavour = CommonMarkFlavourDescriptor()
        val parsedTree = MarkdownParser(flavour).buildMarkdownTreeFromString(markdown)
        visitNode(parsedTree) {
            val text = it.getTextInNode(markdown)
            println(text)
        }
    }

    private fun visitNode(node: ASTNode, visitor: (ASTNode) -> Unit) {
        visitor(node)
        node.children.forEach { visitNode(it, visitor) }
    }
}