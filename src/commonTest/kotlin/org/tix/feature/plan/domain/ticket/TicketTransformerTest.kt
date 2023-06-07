package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.ticket.jira.JiraPlanResult
import org.tix.serialize.dynamic.DynamicElement
import org.tix.test.platform.testEnv
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket
import org.tix.ticket.body.CodeSpanSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class TicketTransformerTest {
    private val renderer = jiraBodyRenderer()

    @Test
    fun ticket_noTransform() {
        val fields = mapOf("field" to "value")
        val ticket = Ticket(
            title = "title",
            body = TicketBody(listOf(CodeSpanSegment("body"))),
            fields = DynamicElement(fields),
            tixId = "tix_1"
        )
        val env = testEnv()
        val context = PlanningContext<JiraPlanResult>()

        val expected = RenderedTicket("title", "{{body}}", fields, "tix_1")
        expect(expected) {
            TicketTransformer(context, env, renderer, ticket).ticket()
        }
    }

    @Test
    fun ticket_transforms() {
        val fields = mapOf(
            "\$var1" to "\$var2",
            "env" to "\$env"
        )
        val ticket = Ticket(
            title = "title \$var1",
            body = TicketBody(listOf(CodeSpanSegment("\$var1"))),
            fields = DynamicElement(fields),
            tixId = "tix_1"
        )
        val env = testEnv("env" to "value3")
        val context = PlanningContext<JiraPlanResult>(
            variables = mapOf(
                "var1" to "value1",
                "var2" to "value2",
                "env" to "\$env"
            )
        )

        val transformedFields = mapOf(
            "value1" to "value2",
            "env" to "value3"
        )
        val expected = RenderedTicket(
            "title value1",
            "{{value1}}",
            transformedFields,
            "tix_1"
        )
        expect(expected) {
            TicketTransformer(context, env, renderer, ticket).ticket()
        }
    }
}