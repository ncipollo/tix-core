package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Worklog(
    val startAt: Int = 0,
    val maxResults: Int = 0,
    val total: Int = 0,
    val worklogs: List<WorklogRecord>
)
