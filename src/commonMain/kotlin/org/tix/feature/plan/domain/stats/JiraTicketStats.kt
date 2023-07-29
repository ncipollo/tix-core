package org.tix.feature.plan.domain.stats

fun jiraTicketStats(startingLevel: Int = 0): TicketStats {
    val labels = if (startingLevel == 1) {
        ticketLabels(
            TicketLevelLabel("story", "stories"),
            TicketLevelLabel("task", "tasks")
        )
    } else {
        ticketLabels(
            TicketLevelLabel("epic", "epics"),
            TicketLevelLabel("story", "stories"),
            TicketLevelLabel("task", "tasks")
        )
    }

    return TicketStats(labels, startingLevel)
}