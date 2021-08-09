package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val progress: Int = 0,
    val total: Int = 0,
    val percent: Int = 0,
)
