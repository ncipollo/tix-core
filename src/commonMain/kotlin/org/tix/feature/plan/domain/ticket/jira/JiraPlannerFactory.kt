package org.tix.feature.plan.domain.ticket.jira

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.TicketPlanner
import org.tix.feature.plan.domain.ticket.TicketPlannerFactory
import org.tix.feature.plan.domain.ticket.dry.DryRunPlanningSystem
import org.tix.integrations.jira.JiraApi
import org.tix.platform.Env

class JiraPlannerFactory(private val env: Env) : TicketPlannerFactory {
    override fun planners(
        shouldDryRun: Boolean,
        tixConfig: TixConfiguration
    ): List<TicketPlanner<*>> = buildList {
        tixConfig.jira?.jiraPlanner(
            shouldDryRun,
            tixConfig.variables,
            tixConfig.variableToken
        )?.let { add(it) }
    }

    private fun JiraConfiguration.jiraPlanner(
        shouldDryRun: Boolean,
        variables: Map<String, String>,
        variableToken: String
    ) =
        TicketPlanner(
            env = env,
            renderer = jiraBodyRenderer(),
            system = jiraSystem(shouldDryRun),
            systemConfig = this,
            variables = variables,
            variableToken = variableToken
        )

    private fun JiraConfiguration.jiraSystem(shouldDryRun: Boolean) =
        if (shouldDryRun) {
            DryRunPlanningSystem(jiraTicketStats(this.startingLevel))
        } else {
            JiraPlanningSystem(JiraApi(this))
        }
}