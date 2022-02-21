package org.tix.feature.plan.domain.stats

import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class JiraTicketStatsTest {
    @Test
    fun render_noEpics_empty() = runTestWorkaround {
        val stats = jiraTicketStats(noEpics = true)
        val expected = """
            Ticket Stats:
            - Total Tickets: 0
            - Stories: 0
            - Tasks: 0
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_noEpics_withAllLevels() = runTestWorkaround {
        val stats = jiraTicketStats(noEpics = true)
        stats.countTicket(0)
        stats.countTicket(1)
        stats.countTicket(1)

        val expected = """
            Ticket Stats:
            - Total Tickets: 3
            - Story: 1
            - Tasks: 2
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_withEpics_empty() = runTestWorkaround {
        val stats = jiraTicketStats(noEpics = false)
        val expected = """
            Ticket Stats:
            - Total Tickets: 0
            - Epics: 0
            - Stories: 0
            - Tasks: 0
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_withEpics_withAllLevels() = runTestWorkaround {
        val stats = jiraTicketStats(noEpics = false)
        repeat(2) { stats.countTicket(0) }
        repeat(3) { stats.countTicket(1) }
        repeat(2) { stats.countTicket(2) }

        val expected = """
            Ticket Stats:
            - Total Tickets: 7
            - Epics: 2
            - Stories: 3
            - Tasks: 2
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }
}