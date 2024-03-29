package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.merge.flatten
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer

class ConfigurationMergeUseCase(
    private val overrideConfigs: List<RawTixConfiguration> = emptyList()
) : FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>> {
    override fun transformFlow(upstream: Flow<List<RawTixConfiguration>>) =
        upstream.map { configs -> mergedResult(configs + overrideConfigs) }

    private fun mergedResult(configs: List<RawTixConfiguration>): FlowResult<RawTixConfiguration> =
        if (configs.isEmpty()) {
            FlowResult.failure(TixConfigurationMergeException)
        } else {
            FlowResult.success(configs.flatten())
        }
}

object TixConfigurationMergeException : RuntimeException("😭 no tix configurations")