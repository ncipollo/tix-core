package org.tix.feature.plan.domain.ticket

interface TicketPlanResult {
    val id: String
    val key: String
    val tixId: String
    val level: Int
    val description: String
    val results: Map<String, String>
    val operation: PlanningOperation
}