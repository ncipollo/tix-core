package org.tix.feature.plan.domain.ticket.github

import kotlin.test.Test
import kotlin.test.expect

class GithubTicketSystemFieldsTest {
    private val expectedFields = setOf(
        "assignees",
        "delete_ticket",
        "milestone",
        "labels",
        "parent",
        "update_ticket"
    )

    @Test
    fun fields() {
        expect("assignees") { GithubTicketSystemFields.assignees }
        expect("milestone") { GithubTicketSystemFields.milestone }
        expect("labels") { GithubTicketSystemFields.labels }
        expect("parent") { GithubTicketSystemFields.parent }
        expect(expectedFields) { GithubTicketSystemFields.fields }
    }
}