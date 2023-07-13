package org.tix.feature.plan.domain.ticket.github

import org.tix.feature.plan.domain.ticket.TicketSystemFields

object GithubTicketSystemFields: TicketSystemFields() {
    val assignees = field("assignees")
    val milestone = field("milestone")
    val labels = field("labels")
    val parent = field("parent")
}