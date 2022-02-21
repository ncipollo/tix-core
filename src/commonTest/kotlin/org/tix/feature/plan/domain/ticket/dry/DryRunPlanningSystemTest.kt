package org.tix.feature.plan.domain.ticket.dry

import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.PlanningCompleteInfo
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.feature.plan.domain.ticket.PlanningOperation
import org.tix.fixture.config.jiraConfig
import org.tix.test.runTestWorkaround
import org.tix.ticket.RenderedTicket
import kotlin.test.Test
import kotlin.test.expect

class DryRunPlanningSystemTest {
    private val ticket = RenderedTicket("title", body = "*body*", fields = mapOf("ticket" to "field"))

    private val config = jiraConfig
    private val ticketStats = jiraTicketStats()
    private val ticketSystem = DryRunPlanningSystem(ticketStats)

    @Test
    fun completeInfo() = runTestWorkaround {
        (0..2).forEach {
            ticketSystem.planTicket(contextForLevel(it), ticket, PlanningOperation.CreateTicket)
            expect(PlanningCompleteInfo(message = ticketStats.render())) {
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
}