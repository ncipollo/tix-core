package org.tix.feature.plan.domain.ticket

import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.dry.DryRunPlanningSystem
import org.tix.feature.plan.domain.ticket.dry.DryRunTicketPlanResult
import org.tix.fixture.config.jiraConfig
import org.tix.test.runTestWorkaround
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertFails

class TicketPlanningSystemTest {
    private val config = jiraConfig
    private val ticketStats = jiraTicketStats()
    private val ticketSystem = DryRunPlanningSystem(ticketStats)

    @Test
    fun validate() = runTestWorkaround {
        assertFails {
            ticketSystem.validate(contextForLevel(0), listOf(deepTicket()))
        }
    }

    private fun contextForLevel(level: Int) =
        PlanningContext<DryRunTicketPlanResult>(config = config, level = level)


    private fun deepTicket(currentDepth: Int = 0): Ticket {
        if (currentDepth >= 3) {
            return Ticket()
        }
        return Ticket(children = listOf(deepTicket(currentDepth + 1)))
    }
}