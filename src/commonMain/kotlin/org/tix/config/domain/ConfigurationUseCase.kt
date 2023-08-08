package org.tix.config.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform

class ConfigurationUseCase(
    private val authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    private val configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    private val configReadUseCase: FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>>,
    private val configMergeUseCase: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>,
) : FlowTransformer<ConfigurationSourceOptions, FlowResult<TixConfiguration>> {
    override fun transformFlow(upstream: Flow<ConfigurationSourceOptions>) =
        upstream.flatMapLatest { config(it) }

    private fun config(configSourceOptions: ConfigurationSourceOptions) =
        flowOf(configSourceOptions)
            .transform(configReadUseCase)
            .transform(configMergeUseCase)
            .flatMapLatest { mergeResult ->
                bakeConfig(mergeResult, configSourceOptions.workspaceDirectory)
            }

    private fun bakeConfig(mergeResult: FlowResult<RawTixConfiguration>, path: String?) =
        if (mergeResult.isSuccess) {
            val rawConfig = mergeResult.getOrThrow()
            flowOf(AuthConfigAction(path, rawConfig))
                .transform(authConfigUseCase)
                .map { ConfigBakerAction(rawConfig, it) }
                .transform(configBakerUseCase)
        } else {
            flowOf(FlowResult.failure(mergeResult.throwable))
        }
}

fun configurationUseCase(
    authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    configReadUseCase: FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>>,
    configMergeUseCase: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>
) = ConfigurationUseCase(authConfigUseCase, configBakerUseCase, configReadUseCase, configMergeUseCase)