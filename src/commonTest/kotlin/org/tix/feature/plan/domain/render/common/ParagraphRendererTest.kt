package org.tix.feature.plan.domain.render.common

import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.ticket.body.ParagraphSegment
import org.tix.ticket.body.TextSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.expect

class ParagraphRendererTest {
    @Test
    fun render() {
        val segment = ParagraphSegment(TicketBody(listOf(TextSegment("text"))) )
        expect("text") { ParagraphRenderer(jiraBodyRenderer()).render(segment) }
    }
}