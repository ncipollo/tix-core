package org.tix.feature.plan.domain.ticket

sealed interface PlanningOperation {
    val ticketKey: String
    object CreateTicket: PlanningOperation {
        override val ticketKey = ""
        override fun toString() = "create"
    }

    data class DeleteTicket(override val ticketKey: String = ""): PlanningOperation {
        override fun toString() = "delete $ticketKey"
    }

    data class UpdateTicket(override val ticketKey: String = ""): PlanningOperation {
        override fun toString() = "update $ticketKey"
    }
}