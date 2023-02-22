package org.tix.feature.plan.domain.ticket.jira.workflow

import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.tix.config.data.Action
import org.tix.feature.plan.domain.ticket.mockPlanningContext
import kotlin.test.expect

class EmptyJiraActionTest {
    @Test
    fun execute() = runTest {
        expect(emptyMap()) {
            EmptyJiraAction.execute(Action(), mockPlanningContext())
        }
    }
}