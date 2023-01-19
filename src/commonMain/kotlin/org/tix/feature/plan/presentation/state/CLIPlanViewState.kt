package org.tix.feature.plan.presentation.state

data class CLIPlanViewState(
    override val isComplete: Boolean = false,
    val message: String = ""
) : PlanViewState {
    override fun toString() = if (isComplete) "done" else message
}
