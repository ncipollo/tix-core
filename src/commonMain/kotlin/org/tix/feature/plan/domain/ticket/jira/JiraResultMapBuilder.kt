package org.tix.feature.plan.domain.ticket.jira

import org.tix.integrations.jira.issue.Issue

fun Issue.resultMap() =
    mapOf(
        "ticket.jira.previous.key" to key,
        "ticket.jira.key" to key
    )