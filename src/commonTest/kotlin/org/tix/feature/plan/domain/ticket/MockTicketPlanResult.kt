package org.tix.feature.plan.domain.ticket

data class MockTicketPlanResult(
    override val id: String = "",
    override val key: String = "",
    override val tixId: String = "",
    override val level: Int = 0,
    override val description: String = "",
    override val results: Map<String, String> = emptyMap(),
    override val operation: PlanningOperation = PlanningOperation.CreateTicket
) : TicketPlanResult
