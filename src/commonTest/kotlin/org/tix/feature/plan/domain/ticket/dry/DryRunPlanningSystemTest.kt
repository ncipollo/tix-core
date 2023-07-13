package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.fixture.config.mockJiraConfig
import org.tix.test.runTestWorkaround
import org.tix.ticket.RenderedTicket
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class DryRunPlanningSystemTest {
    private val ticket = RenderedTicket("title", body = "*body*", fields = mapOf("ticket" to "field"))

    private val config = mockJiraConfig
    private val ticketStats = jiraTicketStats()
    private val ticketSystem = DryRunPlanningSystem(ticketStats)

    @Test
    fun completeInfo() = runTestWorkaround {
        (0..2).forEach {
            ticketSystem.planTicket(contextForLevel(it), ticket, PlanningOperation.CreateTicket)
            expect(PlanningCompleteInfo(message = ticketStats.render(), wasDryRun = true)) {
                ticketSystem.completeInfo()
            }
        }
    }

    @Test
    fun planTicket() = runTestWorkaround {
        val operation = PlanningOperation.CreateTicket
        (0..2).forEach {
            expect(expectedResultForLevel(it, operation)) {
                ticketSystem.planTicket(contextForLevel(it), ticket, operation)
            }
        }
    }

    @Test
    fun validate() = runTestWorkaround {
        val context = PlanningContext<DryRunTicketPlanResult>(config = mockJiraConfig)
        assertFails {
            ticketSystem.validate(context, listOf(deepTicket()))
        }
    }

    private fun contextForLevel(level: Int) =
        PlanningContext<DryRunTicketPlanResult>(config = config, level = level)

    private fun expectedResultForLevel(level: Int, operation: PlanningOperation): DryRunTicketPlanResult {
        val ticketType = ticketStats.capitalizedLabel(level, 1)
        return DryRunTicketPlanResult(
            level = level,
            title = ticket.title,
            ticketType = ticketType,
            body = "*body*",
            fields = ticket.fields,
            operation = operation
        )
    }

    private fun deepTicket(currentDepth: Int = 0): Ticket {
        if (currentDepth >= 3) {
            return Ticket()
        }
        return Ticket(children = listOf(deepTicket(currentDepth + 1)))
    }
}