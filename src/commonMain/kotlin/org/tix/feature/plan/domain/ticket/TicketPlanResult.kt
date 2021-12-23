package org.tix.feature.plan.domain.ticket

interface TicketPlanResult {
    val id: String
    val level: Int
    val description: String
    val results: Map<String, String>
}