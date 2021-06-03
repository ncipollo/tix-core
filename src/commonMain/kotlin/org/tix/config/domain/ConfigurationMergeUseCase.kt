package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.config.data.TixConfiguration
import org.tix.config.merge.flatten
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer

class ConfigurationMergeUseCase(
    private val overrideConfigs: List<TixConfiguration> = emptyList()
) : FlowTransformer<List<TixConfiguration>, FlowResult<TixConfiguration>> {
    override fun transformFlow(upstream: Flow<List<TixConfiguration>>) =
        upstream.map { configs -> mergedResult(configs + overrideConfigs) }

    private fun mergedResult(configs: List<TixConfiguration>): FlowResult<TixConfiguration> =
        if (configs.isEmpty()) {
            FlowResult.failure(TixConfigurationMergeException)
        } else {
            FlowResult.success(configs.flatten())
        }
}

object TixConfigurationMergeException : RuntimeException("😭 no tix configurations")