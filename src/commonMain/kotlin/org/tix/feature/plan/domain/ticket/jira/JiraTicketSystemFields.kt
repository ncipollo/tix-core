package org.tix.feature.plan.domain.ticket.jira

import org.tix.feature.plan.domain.ticket.TicketSystemFields

object JiraTicketSystemFields: TicketSystemFields() {
    val affectsVersions = field("affects_versions")
    val bodyField = field("body_field")
    val components = field("components")
    val fixVersions = field("fix_versions")
    val labels = field("labels")
    val parent = field("parent")
    val priority = field("priority")
    val project = field("project")
    val type = field("type")
    val useParent = field("use_parent")
}