package org.tix.feature.plan.domain.ticket.github

import org.tix.fixture.config.tixConfiguration
import org.tix.test.platform.testEnv
import kotlin.test.Test
import kotlin.test.expect

class GithubPlannerFactoryTest {
    private val env = testEnv()

    @Test
    fun planners_noGithubConfig() {
        val tixConfig = tixConfiguration.copy(github = null)
        val factory = GithubPlannerFactory(env)
        expect(emptyList()) {
            factory.planners(
                shouldDryRun = false,
                tixConfig = tixConfig,
            )
        }
    }

    @Test
    fun planners_returnsDryRunPlanners() {
        val factory = GithubPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = true, tixConfig = tixConfiguration)
        expect(1) { planners.size }
    }

    @Test
    fun planners_returnsNormalPlanners() {
        val factory = GithubPlannerFactory(env)
        val planners = factory.planners(shouldDryRun = false, tixConfig = tixConfiguration)
        expect(1) { planners.size }
    }
}