package org.tix.feature.plan.domain.ticket

import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.render.jira.jiraBodyRenderer
import org.tix.feature.plan.domain.stats.jiraTicketStats
import org.tix.feature.plan.domain.ticket.dry.DryRunPlanningSystem
import org.tix.feature.plan.domain.ticket.jira.JiraPlanningSystem
import org.tix.integrations.jira.JiraApi

class TicketPlannerFactory(
    private val shouldDryRun: Boolean,
    private val tixConfiguration: TixConfiguration
) {
    fun planners() =
        buildList {
            tixConfiguration.jira?.jiraPlanner()?.let { add(it) }
        }

    private fun JiraConfiguration.jiraPlanner() =
        TicketPlanner(
            renderer = jiraBodyRenderer(),
            system = jiraSystem(),
            systemConfig = this,
            variables = tixConfiguration.variables
        )

    private fun JiraConfiguration.jiraSystem() =
        if (shouldDryRun) {
            DryRunPlanningSystem(jiraTicketStats(this.startingLevel))
        } else {
            JiraPlanningSystem(JiraApi(this))
        }
}