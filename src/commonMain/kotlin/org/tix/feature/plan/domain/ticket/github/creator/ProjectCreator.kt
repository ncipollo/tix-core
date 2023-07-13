package org.tix.feature.plan.domain.ticket.github.creator

import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.feature.plan.domain.ticket.github.GithubPlanResult
import org.tix.feature.plan.domain.ticket.github.cache.ProjectCache
import org.tix.feature.plan.domain.ticket.github.cache.RepositoryCache
import org.tix.feature.plan.domain.ticket.github.result.resultMap
import org.tix.integrations.github.GithubApi
import org.tix.integrations.github.graphql.project.ProjectV2Node
import org.tix.ticket.RenderedTicket

class ProjectCreator(
    private val githubApi: GithubApi,
    private val projectCache: ProjectCache,
    private val repositoryCache: RepositoryCache = RepositoryCache(githubApi)
) {
    private val projectQueries = githubApi.queries.projects

    suspend fun planTicket(
        context: PlanningContext<GithubPlanResult>,
        ticket: RenderedTicket,
        operation: PlanningOperation
    ): GithubPlanResult {
        val project = performOperation(ticket, operation)
        return GithubPlanResult(
            id = project.id,
            key = "#${project.number}",
            tixId = ticket.tixId,
            level = context.level,
            description = ticket.title,
            results = project.resultMap(),
            operation = operation,
            startingLevel = context.startingLevel
        )
    }

    private suspend fun performOperation(ticket: RenderedTicket, operation: PlanningOperation) =
        when (operation) {
            PlanningOperation.CreateTicket -> createProject(ticket)
            is PlanningOperation.DeleteTicket -> deleteProject(operation.ticketNumber)
            is PlanningOperation.UpdateTicket -> updateProject(ticket, operation.ticketNumber)
        }

    private suspend fun createProject(ticket: RenderedTicket): ProjectV2Node {
        val repository = repositoryCache.currentRepository()
        val createdProject = projectQueries.createProject(
            repoId = repository.nodeId,
            ownerId = repository.owner.nodeId,
            title = ticket.title
        )
            .data
            .createProjectV2
            .content
        return projectQueries.updateProject(
            projectId = createdProject.id,
            title = ticket.title,
            description = ticket.body
        )
            .data
            .updateProjectV2
            .content
            .also { projectCache.updateProject(it) }
    }

    private suspend fun deleteProject(projectNumber: Long): ProjectV2Node {
        val projectId = projectCache.getProject(projectNumber).id
        return projectQueries.deleteProject(projectId)
            .data
            .deleteProjectV2
            .content
            .also { projectCache.removeProject(projectNumber) }
    }

    private suspend fun updateProject(ticket: RenderedTicket, projectNumber: Long): ProjectV2Node {
        val projectId = projectCache.getProject(projectNumber).id
        return projectQueries.updateProject(
            projectId = projectId,
            title = ticket.title,
            description = ticket.body
        )
            .data
            .updateProjectV2
            .content
            .also { projectCache.updateProject(it) }
    }
}