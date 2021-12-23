package org.tix.feature.plan.domain.ticket

fun mockPlanningContext(level: Int = 0,
                        parentTicket: MockTicketPlanResult? = null,
                        variables: Map<String, String> = emptyMap()) =
    PlanningContext(level, parentTicket, variables)