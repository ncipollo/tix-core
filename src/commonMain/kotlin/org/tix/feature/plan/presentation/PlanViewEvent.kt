package org.tix.feature.plan.presentation

sealed class PlanViewEvent {
    data class PlanUsingMarkdown(val path: String, val shouldDryRun: Boolean) : PlanViewEvent()
}
