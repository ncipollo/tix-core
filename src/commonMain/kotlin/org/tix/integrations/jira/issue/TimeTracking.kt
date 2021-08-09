package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class TimeTracking(
    val originalEstimate: String = "",
    val remainingEstimate: String = "",
    val timeSpend: String = "",
    val originalEstimateSeconds: Int = 0,
    val remainingEstimateSeconds: Int = 0,
    val timeSpentSeconds: Int = 0
)
