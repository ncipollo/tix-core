package org.tix.feature.plan.parse.nodeparser

import org.tix.model.ticket.body.BodySegment
import org.tix.model.ticket.body.EmphasisSegment
import kotlin.test.Test
import kotlin.test.expect

class EmphasisParserTest {
    private val parser = EmphasisParser()

    @Test
    fun parse() {
        val arguments = "*text*".toParserArguments().childArguments!!
        arguments.state.startTicket()

        val results = parser.parse(arguments)

        expect(listOf<BodySegment>(EmphasisSegment("text"))) { arguments.state.currentTicket!!.body }
        expect(1) { results.nextIndex }
    }
}