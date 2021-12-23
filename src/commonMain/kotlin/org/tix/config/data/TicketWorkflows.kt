package org.tix.config.data

data class TicketWorkflows(
    val afterAll: List<Workflow> = emptyList(),
    val afterEach: List<Workflow> = emptyList(),
    val beforeAll: List<Workflow> = emptyList(),
    val beforeEach: List<Workflow> = emptyList()
)
