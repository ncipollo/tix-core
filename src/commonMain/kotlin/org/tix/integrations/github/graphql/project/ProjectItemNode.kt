package org.tix.integrations.github.graphql.project

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import org.tix.integrations.github.graphql.paging.QueryPagedContent

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("itemType")
sealed class ProjectItemNode {
    abstract val id: String
}

@Serializable
@SerialName("DraftIssue")
data class ProjectDraftIssueNode(
    override val id: String = "",
    val assignees: QueryPagedContent<ProjectItemAssigneeNode> = QueryPagedContent(),
    val title: String = "",
    val body: String = ""
): ProjectItemNode()

@Serializable
@SerialName("Issue")
data class ProjectIssueNode(
    override val id: String = "",
    val assignees: QueryPagedContent<ProjectItemAssigneeNode> = QueryPagedContent(),
    val number: Long = 0,
    val title: String = "",
    val body: String = ""
): ProjectItemNode()

@Serializable
@SerialName("PullRequest")
data class ProjectPullRequestNode(
    override val id: String = "",
    val assignees: QueryPagedContent<ProjectItemAssigneeNode> = QueryPagedContent(),
    val number: Long = 0,
    val title: String = "",
    val body: String = ""
): ProjectItemNode()