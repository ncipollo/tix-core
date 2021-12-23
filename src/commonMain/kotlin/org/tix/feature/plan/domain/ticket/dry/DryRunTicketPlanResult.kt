package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.ticket.TicketPlanResult

data class DryRunTicketPlanResult(
    override val id: String = "",
    override val level: Int = 0,
    override val results: Map<String, String> = emptyMap(),
    val title: String,
    val ticketType: String,
    val body: String
) : TicketPlanResult {
    override val description: String
        get() {
            return """
                |-----------------
                |🚀 $ticketType - $title
                |$body
                |-----------------
            """.trimMargin()
        }
}
