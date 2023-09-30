package org.tix.feature.plan.domain.ticket.github

import org.tix.config.data.GithubConfiguration
import org.tix.config.data.MatrixEntryConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.render.github.githubBodyRenderer
import org.tix.feature.plan.domain.stats.githubTicketStats
import org.tix.feature.plan.domain.ticket.MatrixPlanner
import org.tix.feature.plan.domain.ticket.TicketPlanner
import org.tix.feature.plan.domain.ticket.TicketPlannerFactory
import org.tix.feature.plan.domain.ticket.dry.DryRunPlanningSystem
import org.tix.integrations.github.GithubApiFactory
import org.tix.platform.Env

class GithubPlannerFactory(
    private val env: Env,
    private val apiFactory: GithubApiFactory = GithubApiFactory()
) :
    TicketPlannerFactory {
    override fun planners(
        shouldDryRun: Boolean,
        tixConfig: TixConfiguration
    ): List<TicketPlanner<*>> = buildList {
        tixConfig.github?.githubPlanner(
            tixConfig.matrix,
            shouldDryRun,
            tixConfig.variables,
            tixConfig.variableToken
        )?.let { add(it) }
    }

    private fun GithubConfiguration.githubPlanner(
        matrix: Map<String, List<MatrixEntryConfiguration>>,
        shouldDryRun: Boolean,
        variables: Map<String, String>,
        variableToken: String
    ) =
        TicketPlanner(
            env = env,
            matrixPlanner = MatrixPlanner(matrix, variableToken),
            renderer = githubBodyRenderer(),
            system = githubSystem(shouldDryRun),
            systemConfig = this,
            variables = variables,
            variableToken = variableToken
        )

    private fun GithubConfiguration.githubSystem(shouldDryRun: Boolean) =
        if (shouldDryRun) {
            DryRunPlanningSystem(githubTicketStats(this.startingLevel))
        } else {
            GithubPlanningSystem(apiFactory.api(this), githubTicketStats(this.startingLevel))
        }
}