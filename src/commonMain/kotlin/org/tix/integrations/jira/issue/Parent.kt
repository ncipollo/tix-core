package org.tix.integrations.jira.issue

import kotlinx.serialization.Serializable

@Serializable
data class Parent(
    val id: String = "",
    val key: String = ""
)
