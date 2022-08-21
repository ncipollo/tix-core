package org.tix.feature.plan.domain.ticket

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.dry.DryRunPlanningSystem
import org.tix.feature.plan.domain.ticket.jira.JiraPlanningSystem
import org.tix.integrations.jira.JiraApi

interface TicketPlannerFactory {
    fun planners(shouldDryRun: Boolean, tixConfig: TixConfiguration): List<TicketPlanner<*>>
}
class RuntimeTicketPlannerFactory: TicketPlannerFactory {
    override fun planners(shouldDryRun: Boolean, tixConfig: TixConfiguration ) =
        buildList {
            tixConfig.jira?.jiraPlanner(shouldDryRun, tixConfig.variables)?.let { add(it) }
        }

    private fun JiraConfiguration.jiraPlanner(shouldDryRun: Boolean, variables: Map<String, String>) =
        TicketPlanner(
            renderer = jiraBodyRenderer(),
            system = jiraSystem(shouldDryRun),
            systemConfig = this,
            variables = variables
        )

    private fun JiraConfiguration.jiraSystem(shouldDryRun: Boolean) =
        if (shouldDryRun) {
            DryRunPlanningSystem(jiraTicketStats(this.startingLevel))
        } else {
            JiraPlanningSystem(JiraApi(this))
        }
}

fun ticketPlannerFactory() = RuntimeTicketPlannerFactory()