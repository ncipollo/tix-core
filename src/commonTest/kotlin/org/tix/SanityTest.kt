package org.tix

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.mamoe.yamlkt.Yaml
import org.intellij.markdown.ast.ASTNode
import org.intellij.markdown.ast.acceptChildren
import org.intellij.markdown.ast.getTextInNode
import org.intellij.markdown.ast.visitors.Visitor
import org.intellij.markdown.flavours.commonmark.CommonMarkFlavourDescriptor
import org.intellij.markdown.parser.MarkdownParser
import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.BlockQuoteSegment
import org.tix.model.ticket.body.TextSegment
import kotlin.test.Test
import kotlin.test.expect

class SanityTest {
    private val markdown =
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

        val ticket = Ticket(title = "My Ticket", body = listOf(BlockQuoteSegment, TextSegment("text")))
        val json = Json.encodeToString(ticket)
        println(json)

        val testObj = YmlObject(test = "test", nested = Nested(items = listOf("one", "two")))
        val yml = Yaml.Default.encodeToString(testObj)
        println(yml)
    }

    @Serializable
    data class YmlObject(val test: String, val nested:Nested)

    @Serializable
    data class Nested(val items: List<String>)

    private fun visitNode(node: ASTNode, visitor: (ASTNode) -> Unit) {
        visitor(node)
        node.children.forEach { visitNode(it, visitor) }
    }
}