package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.config.bake.GithubConfigurationBaker
import org.tix.config.bake.JiraConfigurationBaker
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.ext.toFlowResult

class ConfigurationBakerUseCase : FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>> {

    override fun transformFlow(upstream: Flow<ConfigBakerAction>): Flow<FlowResult<TixConfiguration>> =
        upstream.map { bake(it.rawConfig, it.ticketSystemAuth) }

    private fun bake(rawConfig: RawTixConfiguration, ticketSystemAuth: TicketSystemAuth) =
        runCatching {
            TixConfiguration(
                include = rawConfig.include,
                github = GithubConfigurationBaker.bake(rawConfig.github, ticketSystemAuth.github),
                jira = JiraConfigurationBaker.bake(rawConfig.jira, ticketSystemAuth.jira),
                variables = rawConfig.variables
            )
        }.toFlowResult()
}

data class ConfigBakerAction(val rawConfig: RawTixConfiguration, val ticketSystemAuth: TicketSystemAuth)