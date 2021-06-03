package org.tix.feature.plan.presentation

data class PlanErrorState(val errorMessage: String = "")

data class PlanTicketState(val status: String = "", val tickets: List<PlanTicketState> = emptyList())

data class PlanViewState(
    val complete: Boolean = true,
    val errorState: PlanErrorState = PlanErrorState(),
    val tickets: List<PlanTicketState> = emptyList()
)