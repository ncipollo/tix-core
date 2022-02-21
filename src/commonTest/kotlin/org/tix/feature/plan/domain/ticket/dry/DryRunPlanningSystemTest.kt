package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.test.runTestWorkaround
import org.tix.ticket.Ticket
import org.tix.ticket.body.StrongEmphasisSegment
import org.tix.ticket.body.TicketBody
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class DryRunPlanningSystemTest {
    private val body = TicketBody(listOf(StrongEmphasisSegment("body")))
    private val ticket = Ticket("title", body = body)

    private val renderer = jiraBodyRenderer()
    private val ticketStats = jiraTicketStats(noEpics = false)
    private val ticketSystem = DryRunPlanningSystem(renderer, ticketStats)

    @Test
    fun completeInfo() = runTestWorkaround {
        (0..2).forEach {
            ticketSystem.planTicket(contextForLevel(it), ticket)
            expect(PlanningCompleteInfo(message = ticketStats.render())) {
                ticketSystem.completeInfo()
            }
        }
    }

    @Test
    fun planTicket() = runTestWorkaround {
        (0..2).forEach {
            expect(expectedResultForLevel(it)) {
                ticketSystem.planTicket(contextForLevel(it), ticket)
            }
        }
    }

    @Test
    fun validate() = runTestWorkaround {
        assertFails {
            ticketSystem.validate(listOf(deepTicket()))
        }
    }

    private fun contextForLevel(level: Int) = PlanningContext<DryRunTicketPlanResult>(level)

    private fun expectedResultForLevel(level: Int): DryRunTicketPlanResult {
        val ticketType = ticketStats.capitalizedLabel(level, 1)
        return DryRunTicketPlanResult(
            level = level,
            title = ticket.title,
            ticketType = ticketType,
            body = "*body*"
        )
    }

    private fun deepTicket(currentDepth: Int = 0): Ticket {
        if (currentDepth >= 3) {
            return Ticket()
        }
        return Ticket(children = listOf(deepTicket(currentDepth + 1)))
    }
}