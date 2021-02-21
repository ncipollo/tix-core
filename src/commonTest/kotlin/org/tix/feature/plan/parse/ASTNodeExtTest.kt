package org.tix.feature.plan.parse

import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.findChildOfType
import kotlin.test.Test
import kotlin.test.expect

class ASTNodeExtTest {
    private val markdown = """
        # Section 1
        Body 1
        ## Section 2
        Body 2
    """.trimIndent()
    private val rootNode = defaultMarkdownParser().buildMarkdownTreeFromString(markdown)

    @Test
    fun lineNumber_returnsFirstLine() {
        val node = rootNode.children[0]
        expect(0) { node.lineNumber(markdown) }
    }

    @Test
    fun lineNumber_returnsThirdLine() {
        val node = rootNode.findChildOfType(MarkdownElementTypes.ATX_2)!!
        expect(2) { node.lineNumber(markdown) }
    }
}