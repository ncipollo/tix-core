package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.ticket.jira.JiraPlanResult
import org.tix.serialize.dynamic.DynamicElement
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket
import org.tix.ticket.body.TextSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class TicketTransformerTest {
    private val context = PlanningContext<JiraPlanResult>()
    private val fields = mapOf("field" to "value")
    private val renderer = jiraBodyRenderer()
    private val ticket = Ticket(
        title = "title",
        body = TicketBody(listOf(TextSegment("body"))),
        fields = DynamicElement(fields),
        tixId = "tix_1"
    )

    @Test
    fun ticket() {
        val expected = RenderedTicket("title", "body", fields, "tix_1")
        expect(expected) {
            TicketTransformer(context, renderer, ticket).ticket()
        }
    }
}