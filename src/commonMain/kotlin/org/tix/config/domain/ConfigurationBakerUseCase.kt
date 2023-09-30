package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.config.bake.GithubConfigurationBaker
import org.tix.config.bake.JiraConfigurationBaker
import org.tix.config.bake.MatrixBaker
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
                github = githubConfig(rawConfig, ticketSystemAuth),
                jira = jiraConfig(rawConfig, ticketSystemAuth),
                matrix = MatrixBaker.bake(rawConfig.matrix),
                variables = rawConfig.variables,
                variableToken = rawConfig.variableToken ?: "$"
            )
        }.toFlowResult()

    private fun githubConfig(rawConfig: RawTixConfiguration, ticketSystemAuth: TicketSystemAuth) =
        rawConfig.github?.let { GithubConfigurationBaker.bake(it, ticketSystemAuth.github) }

    private fun jiraConfig(rawConfig: RawTixConfiguration, ticketSystemAuth: TicketSystemAuth) =
        rawConfig.jira?.let { JiraConfigurationBaker.bake(it, ticketSystemAuth.jira) }
}

data class ConfigBakerAction(val rawConfig: RawTixConfiguration, val ticketSystemAuth: TicketSystemAuth)