package org.tix.feature.info.fields

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.tix.config.data.JiraConfiguration
import org.tix.config.data.TixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.integrations.jira.JiraApiFactory

class FieldInfoFetcher(
    private val configUseCase: FlowTransformer<ConfigurationSourceOptions, FlowResult<TixConfiguration>>,
    private val jiraApiFactory: JiraApiFactory = JiraApiFactory()
) {
    suspend fun fetchFields(configOptions: ConfigurationSourceOptions) =
        flowOf(configOptions)
            .transform(configUseCase)
            .map { configToFields(it) }
            .first()

    private suspend fun configToFields(configResult: FlowResult<TixConfiguration>) =
        configResult.map { tixConfig ->
            tixConfig.jira?.let { fields(it) } ?: emptyList()
        }

    private suspend fun fields(jiraConfig: JiraConfiguration) =
        jiraApiFactory.api(jiraConfig)
            .field
            .fields()
}