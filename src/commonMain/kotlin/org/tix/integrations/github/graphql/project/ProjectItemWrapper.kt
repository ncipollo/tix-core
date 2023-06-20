package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectItemWrapper(val item: ProjectItemNode)
