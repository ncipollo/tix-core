package org.tix.feature.plan.domain.ticket.jira

import kotlin.test.Test
import kotlin.test.expect

class JiraTicketSystemFieldsTest {
    private val expectedFields = setOf(
        "affects_versions",
        "components",
        "delete_ticket",
        "fix_versions",
        "labels",
        "parent",
        "project",
        "type",
        "use_parent",
        "update_ticket"
    )

    @Test
    fun fields() {
        expect("affects_versions") { JiraTicketSystemFields.affectsVersions }
        expect("components") { JiraTicketSystemFields.components }
        expect("fix_versions") { JiraTicketSystemFields.fixVersions }
        expect("labels") { JiraTicketSystemFields.labels }
        expect("project") { JiraTicketSystemFields.project }
        expect("type") { JiraTicketSystemFields.type }
        expect("parent") { JiraTicketSystemFields.parent }
        expect("use_parent") { JiraTicketSystemFields.useParent }
        expect("update_ticket") { JiraTicketSystemFields.updateTicket }
        expect(expectedFields) { JiraTicketSystemFields.fields }
    }
}