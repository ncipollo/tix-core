package org.tix.integrations.github.rest.issue

import kotlinx.serialization.Serializable
import org.tix.integrations.github.rest.label.Label
import org.tix.integrations.github.rest.milestone.Milestone
import org.tix.integrations.github.rest.user.User
import org.tix.integrations.github.state.State

@Serializable
data class Issue(
    val id: Long = 0,
    val nodeId: String = "",
    val url: String? = null,
    val repositoryUrl: String? = null,
    val labelsUrl: String = "",
    val commentsUrl: String = "",
    val eventsUrl: String = "",
    val htmlUrl: String = "",
    val number: Long = 0,
    val state: State = State.OPEN,
    val title: String = "",
    val body: String? = "",
    val labels: List<Label> = emptyList(),
    val assignee: User? = null,
    val assignees: List<User>? = null,
    val milestone: Milestone? = null
)
