package org.tix.feature.plan.domain.ticket.jira

import org.tix.fixture.config.tixConfiguration
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class JiraPlannerFactoryTest {
    private val env = testEnv()

    @Test
    fun planners_noJiraConfig() {
        val tixConfig = tixConfiguration.copy(jira = null)
        val factory = JiraPlannerFactory(env)
        expect(emptyList()) {
            factory.planners(
                shouldDryRun = false,
                tixConfig = tixConfig,
            )
        }
    }

    @Test
    fun planners_returnsDryRunPlanners() {
        val factory = JiraPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = true, tixConfig = tixConfiguration)
        expect(1) { planners.size }
    }

    @Test
    fun planners_returnsNormalPlanners() {
        val factory = JiraPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = false, tixConfig = tixConfiguration)
        expect(1) { planners.size }
    }
}