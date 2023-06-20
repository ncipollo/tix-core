package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.fixture.config.mockJiraConfig
import org.tix.test.platform.testEnv
import kotlin.test.expect

class MockTicketPlannerFactory(private val count: Int) : TicketPlannerFactory {
    private val env = testEnv()
    var shouldDryRun: Boolean? = null
    var tixConfig: TixConfiguration? = null

    override fun planners(shouldDryRun: Boolean, tixConfig: TixConfiguration) =
        List(count) {
            TicketPlanner(
                env = env,
                renderer = jiraBodyRenderer(),
                system = MockTicketPlanningSystem(),
                systemConfig = mockJiraConfig,
                variables = emptyMap(),
                variableToken = "$"
            )
        }.also {
            this.shouldDryRun = shouldDryRun
            this.tixConfig = tixConfig
        }

    fun assertPlannersCalled(shouldDryRun: Boolean, tixConfig: TixConfiguration) {
        expect(shouldDryRun) { this.shouldDryRun }
        expect(tixConfig) { this.tixConfig }
    }
}