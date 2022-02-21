package org.tix.feature.plan.domain.ticket.jira

import org.tix.ext.transform
import org.tix.ext.transformFilteredList
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.issue.Component
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.issue.Parent
import org.tix.integrations.jira.project.Project
import org.tix.integrations.jira.version.Version

class JiraIssueFieldsBuilder(
    private val context: PlanningContext<JiraPlanResult>,
    private val summary: String,
    private val description: String,
    private val fields: Map<String, Any?>,
    private val unknownsBuilder: JiraUnknownsBuilder,
) {
    fun issueFields() = IssueFields(
        affectsVersions = affectsVersions(),
        components = components(),
        description = description,
        fixVersions = fixVersions(),
        labels = labels(),
        parent = parent(),
        project = project(),
        summary = summary,
        type = type(),
        unknowns = unknownsBuilder.unknowns(fields)
    )

    private fun affectsVersions() =
        fields.transformFilteredList<String, Version>(JiraTicketSystemFields.affectsVersions) { Version(name = it) }

    private fun components() =
        fields.transformFilteredList<String, Component>(JiraTicketSystemFields.components) { Component(name = it) }

    private fun fixVersions() =
        fields.transformFilteredList<String, Version>(JiraTicketSystemFields.fixVersions) { Version(name = it) }

    private fun labels() =
        fields.transformFilteredList<String, String>(JiraTicketSystemFields.labels) { it }

    private fun parent(): Parent? {
        val default: String? = null
        val explicitParent = fields.transform(JiraTicketSystemFields.parent, default) { key ->
            key?.let { Parent(key = it) }
        }
        val implicitParent = context.parentTicket
            ?.id
            ?.takeIf { it.isNotBlank() }
            ?.let { Parent(id = it) }
        return explicitParent ?: implicitParent
    }

    private fun project() = fields.transform(JiraTicketSystemFields.project, "") { Project(key = it) }

    private fun type() = fields.transform(JiraTicketSystemFields.type, defaultType()) { IssueType(name = it) }

    private fun defaultType(): String =
        when (context.level) {
            0 -> "Epic"
            1 -> "Story"
            else -> "Subtask"
        }
}