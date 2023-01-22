package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.TicketPlanResult

data class DryRunTicketPlanResult(
    override val id: String = "",
    override val key: String = "",
    override val tixId: String = "",
    override val level: Int = 0,
    override val results: Map<String, String> = emptyMap(),
    val title: String,
    val ticketType: String,
    val body: String,
    val fields: Map<String, Any?>,
    override val operation: PlanningOperation,
    override val startingLevel: Int = 0
) : TicketPlanResult {
    override val description: String
        get() {
            return """
                |-----------------
                |🚀 $ticketType - $title
                |$body
                |-----------------
                |Fields:
                $fieldsDescription
                |-----------------
                |Operation: $operation
                |-----------------
            """.trimMargin()
        }

    private val fieldsDescription =
        fields.keys.sorted()
            .joinToString(separator = "\n") { "|- $it = ${fields[it]}" }
}
