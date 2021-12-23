package org.tix.feature.plan.domain.stats

fun githubTicketStats(noProjects: Boolean): TicketStats {
    val labels = if (noProjects) {
        ticketLabels(TicketLevelLabel("issue", "issues"))
    } else {
        ticketLabels(
            TicketLevelLabel("project", "projects"),
            TicketLevelLabel("issue", "issues")
        )
    }
    return TicketStats(labels)
}