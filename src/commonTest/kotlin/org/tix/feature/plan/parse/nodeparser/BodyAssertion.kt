package org.tix.feature.plan.parse.nodeparser

import org.tix.feature.plan.parse.state.PartialTicket
import org.tix.model.ticket.Ticket
import org.tix.model.ticket.body.*
import kotlin.test.expect

internal class ExpectedBodyBuilder {
    private val segments = mutableListOf<BodySegment>()

    val body = segments.toTicketBody()

    fun codeBlock(code: String = "", language: String = "") {
        segments += CodeBlockSegment(code = code, language = language)
    }

    fun emphasis(text: String = "") {
        segments += EmphasisSegment(text = text)
    }

    fun linebreak() {
        segments += LinebreakSegment
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

    fun whitespace(count: Int = 1) {
        segments += WhitespaceSegment(count)
    }
}

internal fun expectBody(ticket: Ticket, builderBlock: ExpectedBodyBuilder.() -> Unit) {
    expect(ticket.body) {
        ExpectedBodyBuilder().apply(builderBlock).body
    }
}

internal fun expectBody(partialTicket: PartialTicket?, builderBlock: ExpectedBodyBuilder.() -> Unit) =
    expectBody(partialTicket!!.ticket(), builderBlock)

internal fun expectBody(arguments: ParserArguments, builderBlock: ExpectedBodyBuilder.() -> Unit) =
    expectBody(arguments.state.currentTicket, builderBlock)