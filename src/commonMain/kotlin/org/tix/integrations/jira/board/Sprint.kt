package org.tix.integrations.jira.board

import kotlinx.serialization.Serializable

@Serializable
data class Sprint(
    val id: Int = 0,
    val name: String = "",
    val completedDate: Long? = null,
    val endDate: Long? = null,
    val startDate: Long? = null,
    val originalBoardId: Int = 0,
    val self: String = "",
    val state: String = ""
)
