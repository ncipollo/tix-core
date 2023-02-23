package org.tix.feature.plan.presentation.state

data class CLIPlanViewState(
    override val isComplete: Boolean = false,
    val message: String = "",
    val mood: String = "✅"
) : PlanViewState {
    override fun toString() = message
}
