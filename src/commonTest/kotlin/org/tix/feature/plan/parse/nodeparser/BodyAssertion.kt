package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.state.PartialTicket
import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.*
import kotlin.test.expect

internal class ExpectedBodyBuilder {
    private val segments = mutableListOf<BodySegment>()

    val body = segments.toTicketBody()

    fun blockQuote(builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += BlockQuoteSegment(body = ExpectedBodyBuilder().apply(builderBlock).body)
    }

    fun bulletList(level: Int = 0, builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += BulletListSegment(body = ExpectedBodyBuilder().apply(builderBlock).body, level)
    }

    fun bulletListItem(level: Int = 0, marker: String = "-", builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += BulletListItemSegment(
            body = ExpectedBodyBuilder().apply(builderBlock).body,
            level = level,
            marker = marker
        )
    }

    fun codeBlock(code: String = "", language: String = "") {
        segments += CodeBlockSegment(code = code, language = language)
    }

    fun codeSpan(code: String = "") {
        segments += CodeSpanSegment(code = code)
    }

    fun emphasis(text: String = "") {
        segments += EmphasisSegment(text = text)
    }

    fun linebreak() {
        segments += LinebreakSegment
    }

    fun link(destination: String = "", title: String = "") {
        segments += LinkSegment(destination, title)
    }

    fun orderedList(level: Int = 0, builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += OrderedListSegment(body = ExpectedBodyBuilder().apply(builderBlock).body, level)
    }

    fun orderedListItem(level: Int = 0, number: Int = 1, builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += OrderedListItemSegment(
            body = ExpectedBodyBuilder().apply(builderBlock).body,
            level = level,
            number = number
        )
    }

    fun paragraph(builderBlock: ExpectedBodyBuilder.() -> Unit) {
        segments += ParagraphSegment(body = ExpectedBodyBuilder().apply(builderBlock).body)
    }

    fun strongEmphasis(text: String = "") {
        segments += StrongEmphasisSegment(text = text)
    }

    fun text(text: String = "") {
        segments += TextSegment(text)
    }

    fun thematicBreak() {
        segments += ThematicBreakSegment
    }

    fun whitespace(count: Int = 1) {
        segments += WhitespaceSegment(count)
    }
}

internal fun expectBody(ticket: Ticket, builderBlock: ExpectedBodyBuilder.() -> Unit) {
    expect(ExpectedBodyBuilder().apply(builderBlock).body) {
        ticket.body
    }
}

internal fun expectBody(partialTicket: PartialTicket?, builderBlock: ExpectedBodyBuilder.() -> Unit) =
    expectBody(partialTicket!!.ticket(), builderBlock)

internal fun expectBody(arguments: ParserArguments, builderBlock: ExpectedBodyBuilder.() -> Unit) =
    expectBody(arguments.state.currentTicket, builderBlock)