package org.tix.feature.plan.domain.ticket.github.result

import org.tix.integrations.github.graphql.project.ProjectV2Node
import org.tix.integrations.github.rest.issue.Issue

fun ProjectV2Node.resultMap() =
    mapOf(
        "ticket.github.previous.key" to "#$number",
        "ticket.github.key" to "#$number"
    )

fun Issue.resultMap() =
    mapOf(
        "ticket.github.previous.key" to "#$number",
        "ticket.github.key" to "#$number"
    )