package org.tix.feature.plan.domain.ticket.jira

import org.tix.ext.optionalTransform
import org.tix.ext.transform
import org.tix.ext.transformFilteredList
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.issue.Component
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.issue.Parent
import org.tix.integrations.jira.priority.Priority
import org.tix.integrations.jira.project.Project
import org.tix.integrations.jira.version.Version

class JiraIssueFieldsBuilder(
    private val context: PlanningContext<JiraPlanResult>,
    private val summary: String,
    private val description: String,
    private val fields: Map<String, Any?>,
    private val unknownsBuilder: JiraUnknownsBuilder,
) {
    private val bodyField = fields.transform<String?, String?>(JiraTicketSystemFields.bodyField, null) { it }
    private val modifiedFields = if (!bodyField.isNullOrBlank()) {
        fields + mapOf(bodyField to description)
    } else {
        fields
    }

    fun issueFields() = IssueFields(
        affectsVersions = affectsVersions(),
        components = components(),
        description = description.takeIf { bodyField.isNullOrBlank() },
        fixVersions = fixVersions(),
        labels = labels(),
        parent = parent(),
        project = project(),
        priority = priority(),
        summary = summary,
        type = type(),
        unknowns = unknownsBuilder.unknowns(modifiedFields)
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

    private fun priority() =
        fields.optionalTransform<String, Priority>(JiraTicketSystemFields.priority) { Priority(name = it) }

    private fun project() = fields.transform(JiraTicketSystemFields.project, "") { Project(key = it) }

    private fun type() = fields.transform(JiraTicketSystemFields.type, defaultType()) { IssueType(name = it) }

    private fun defaultType(): String =
        when (context.level) {
            0 -> "Epic"
            1 -> "Story"
            else -> "Subtask"
        }
}