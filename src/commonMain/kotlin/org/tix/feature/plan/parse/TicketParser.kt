package org.tix.feature.plan.parse

import org.intellij.markdown.parser.MarkdownParser
import org.tix.model.ticket.Ticket

class TicketParser(private val markdownParser: MarkdownParser = defaultMarkdownParser()) {
    fun parse(markdown: String): List<Ticket> {
        val nodes = markdownParser.buildMarkdownTreeFromString(markdown)
        return emptyList()
    }
}