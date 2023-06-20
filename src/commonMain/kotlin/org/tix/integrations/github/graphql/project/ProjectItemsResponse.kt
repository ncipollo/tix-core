package org.tix.integrations.github.graphql.project

import kotlinx.serialization.Serializable
import org.tix.integrations.github.graphql.repository.PagedRepository

@Serializable
data class ProjectItemsResponse(val repository: PagedRepositoryProjectItems)

@Serializable
data class PagedRepositoryProjectItems(val projectV2: PagedRepository<ProjectItemWrapper>)