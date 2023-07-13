package org.tix.feature.plan.domain.ticket.github.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.project.ProjectV2Node

class ProjectCache(githubApi: GithubApi) {
    private val mutex = Mutex()
    private val projectsByNumber = mutableMapOf<Long, ProjectV2Node>()
    private val projectApi = githubApi.queries.projects

    suspend fun getProject(number: Long) =
        mutex.withLock {
            val cached = projectsByNumber[number]
            cached ?: fetchProject(number)
                .also { projectsByNumber[number] = it }
        }

    suspend fun updateProject(project: ProjectV2Node) {
        mutex.withLock {
            projectsByNumber[project.number] = project
        }
    }

    suspend fun removeProject(projectNumber: Long) {
        mutex.withLock {
            projectsByNumber.remove(projectNumber)
        }
    }

    private suspend fun fetchProject(number: Long) =
        projectApi.repoProject(number).data.repository.projectV2
}