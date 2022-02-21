package org.tix.feature.plan.domain.stats

fun githubTicketStats(startingLevel: Int = 0): TicketStats {
    val labels = if (startingLevel == 1) {
        ticketLabels(TicketLevelLabel("issue", "issues"))
    } else {
        ticketLabels(
            TicketLevelLabel("project", "projects"),
            TicketLevelLabel("issue", "issues")
        )
    }
    return TicketStats(labels)
}