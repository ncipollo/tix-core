package org.tix.feature.plan.domain.stats

import org.tix.test.runTestWorkaround
import kotlin.test.Test
import kotlin.test.expect

class GithubTicketStatsTest {
    @Test
    fun render_withStartingLevel1_empty() = runTestWorkaround {
        val stats = githubTicketStats(startingLevel = 1)
        val expected = """
            Ticket Stats:
            - Total Tickets: 0
            - Issues: 0
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_withStartingLevel1_withAllLevels() = runTestWorkaround {
        val stats = githubTicketStats(startingLevel = 1)
        stats.countTicket(0)
        stats.countTicket(0)

        val expected = """
            Ticket Stats:
            - Total Tickets: 2
            - Issues: 2
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_withStartingLevel0_empty() = runTestWorkaround {
        val stats = githubTicketStats(startingLevel = 0)
        val expected = """
            Ticket Stats:
            - Total Tickets: 0
            - Projects: 0
            - Issues: 0
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }

    @Test
    fun render_withStartingLevel0_withAllLevels() = runTestWorkaround {
        val stats = githubTicketStats(startingLevel = 0)
        repeat(2) { stats.countTicket(0) }
        repeat(3) { stats.countTicket(1) }

        val expected = """
            Ticket Stats:
            - Total Tickets: 5
            - Projects: 2
            - Issues: 3
        """.trimIndent()
        expect(expected) {
            stats.render()
        }
    }
}