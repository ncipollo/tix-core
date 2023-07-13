package org.tix.feature.plan.domain.ticket

import org.tix.config.data.TixConfiguration
import org.tix.feature.plan.domain.ticket.github.GithubPlannerFactory
import org.tix.feature.plan.domain.ticket.jira.JiraPlannerFactory
import org.tix.platform.Env

interface TicketPlannerFactory {
    fun planners(shouldDryRun: Boolean, tixConfig: TixConfiguration): List<TicketPlanner<*>>
}

class RuntimeTicketPlannerFactory(env: Env) : TicketPlannerFactory {
    private val plannerFactories = listOf(
        GithubPlannerFactory(env),
        JiraPlannerFactory(env)
    )

    override fun planners(shouldDryRun: Boolean, tixConfig: TixConfiguration) =
        plannerFactories.flatMap { it.planners(shouldDryRun, tixConfig) }
}

fun ticketPlannerFactory(env: Env) = RuntimeTicketPlannerFactory(env)