package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectItemAssigneeNode(val id: String = "", val login: String = "", val name: String)
