package org.tix.feature.plan.domain.stats

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TicketStats(private val labels: TicketLabels,
                  private val startingLevel: Int) : LevelLabels by labels {
    private val mutex = Mutex()
    private val countByLevel = MutableList(labels.levelCount) { 0 }

    suspend fun countTicket(level: Int) {
        val adjustedLevel = level - startingLevel
        mutex.withLock {
            countByLevel[adjustedLevel]++
        }
    }

    suspend fun render(): String = mutex.withLock {
        var stats = "Ticket Stats:\n"
        stats += "- Total Tickets: ${total()}\n"
        stats += levelStats()
        return stats
    }

    private fun total() =
        if (countByLevel.isNotEmpty()) {
            countByLevel.reduce { acc, count -> acc + count }
        } else {
            0
        }

    private fun levelStats() =
        countByLevel
            .mapIndexed { level, count ->
                "- ${labels.capitalizedLabel(level, count)}: $count"
            }
            .joinToString(separator = "\n")
}