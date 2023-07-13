package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(val repository: ProjectV2Wrapper)

@Serializable
data class ProjectV2Wrapper(val projectV2: ProjectV2Node)