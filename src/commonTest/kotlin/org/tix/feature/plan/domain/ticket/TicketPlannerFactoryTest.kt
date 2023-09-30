package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TixConfiguration
import org.tix.fixture.config.tixConfiguration
import org.tix.serialize.dynamic.emptyDynamic
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class TicketPlannerFactoryTest {
    private val env = testEnv()

    @Test
    fun planners_noSystemConfigs_returnsNoPlanners() {
        val tixConfig = TixConfiguration(
            include = emptyDynamic(),
            github = null,
            jira = null,
            matrix = emptyMap(),
            variables = emptyMap(),
            variableToken = "$"
        )
        val factory = ticketPlannerFactory(env)
        expect(emptyList()) {
            factory.planners(
                shouldDryRun = false,
                tixConfig = tixConfig,
            )
        }
    }

    @Test
    fun planners_returnsDryRunPlanners() {
        val factory = ticketPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = true, tixConfig = tixConfiguration)
        expect(2) { planners.size }
    }

    @Test
    fun planners_returnsNormalPlanners() {
        val factory = ticketPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = false, tixConfig = tixConfiguration)
        expect(2) { planners.size }
    }
}