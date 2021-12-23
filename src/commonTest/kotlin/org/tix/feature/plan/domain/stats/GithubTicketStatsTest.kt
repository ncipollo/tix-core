package org.tix.feature.plan.domain.stats

import org.tix.test.runTestForNative
import kotlin.test.Test
import kotlin.test.expect

class GithubTicketStatsTest {
    @Test
    fun render_noProjects_empty() = runTestForNative {
        val stats = githubTicketStats(noProjects = true)
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
    fun render_noProjects_withAllLevels() = runTestForNative {
        val stats = githubTicketStats(noProjects = true)
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
    fun render_withProjects_empty() = runTestForNative {
        val stats = githubTicketStats(noProjects = false)
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
    fun render_withProjects_withAllLevels() = runTestForNative {
        val stats = githubTicketStats(noProjects = false)
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