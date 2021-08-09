package org.tix.integrations.jira.transition

import kotlinx.serialization.Serializable

@Serializable
data class TransitionField(val required: Boolean = false)
