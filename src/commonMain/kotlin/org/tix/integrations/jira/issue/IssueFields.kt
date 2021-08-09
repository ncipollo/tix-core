package org.tix.integrations.jira.issue

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import org.tix.integrations.jira.board.Sprint
import org.tix.integrations.jira.priority.Priority
import org.tix.integrations.jira.project.Project
import org.tix.integrations.jira.status.Status
import org.tix.integrations.jira.user.User
import org.tix.integrations.jira.version.Version

@Serializable
data class IssueFields(
    val expand: String = "",
    @SerialName("issuetype")
    val type: IssueType = IssueType(),
    val project: Project = Project(),
    val priority: Priority? = null,
    @SerialName("resolutiondate")
    val resolutionDate: Long? = null,
    val created: String = "",
    @SerialName("duedate")
    val dueDate: Long? = null,
    val watches: Watches? = null,
    val assignee: User? = null,
    val updated: String = "",
    val description: String = "",
    val summary: String = "",
    val creator: User? = null,
    val reporter: User? = null,
    val components: List<Component> = emptyList(),
    val status: Status? = null,
    val progress: Progress? = null,
    @SerialName("aggregateprogress")
    val aggregateProgress: Progress? = null,
    val timeTracking: TimeTracking? = null,
    @SerialName("timespent")
    val timeSpent: Int? = null,
    @SerialName("timeestimate")
    val timeEstimate: Int? = null,
    val timeOriginalEstimate: Int? = null,
    val worklog: Worklog? = null,
    val issueLinks: List<IssueLink> = emptyList(),
    val comments: Comments? = null,
    val fixVersions: List<Version> = emptyList(),
    @SerialName("versions")
    val affectsVersions: List<Version> = emptyList(),
    val labels: List<String> = emptyList(),
    val subtasks: List<Subtask> = emptyList(),
    val attachments: List<Attachment> = emptyList(),
    val epic: Epic? = null,
    val sprint: Sprint? = null,
    val parent: Parent? = null,
    @SerialName("aggregatetimeoriginalestimate")
    val aggregateTimeOriginalEstimate: Int? = null,
    @SerialName("aggregatetimespent")
    val aggregateTimeSpent: Int? = null,
    @SerialName("aggregatetimeestimate")
    val aggregateTimeEstimate: Int? = null,
    val unknowns: JsonObject = JsonObject(emptyMap())
)
