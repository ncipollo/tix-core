package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TicketSystemConfiguration
import org.tix.config.data.emptyTicketSystemConfig

fun mockPlanningContext(
    level: Int = 0,
    parentTicket: MockTicketPlanResult? = null,
    config: TicketSystemConfiguration = emptyTicketSystemConfig(),
    variables: Map<String, String> = emptyMap()
) = PlanningContext(
    level = level,
    parentTicket = parentTicket,
    config = config,
    variables = variables
)