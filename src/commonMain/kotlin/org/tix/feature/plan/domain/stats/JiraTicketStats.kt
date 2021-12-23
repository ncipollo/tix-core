package org.tix.feature.plan.domain.stats

fun jiraTicketStats(noEpics: Boolean) : TicketStats {
    val labels = if(noEpics) {
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

    return TicketStats(labels)
}