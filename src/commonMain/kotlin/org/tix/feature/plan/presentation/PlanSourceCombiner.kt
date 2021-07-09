package org.tix.feature.plan.presentation

import kotlinx.coroutines.flow.*
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.AuthConfigAction
import org.tix.config.domain.ConfigBakerAction
import org.tix.config.domain.TicketSystemAuth
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.TixError
import org.tix.error.toTixError

class PlanSourceCombiner(
    private val authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    private val configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    private val configReadSource: FlowTransformer<String, List<RawTixConfiguration>>,
    private val configMergeSource: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>,
    private val markdownSource: FlowTransformer<String, FlowResult<String>>,
) : FlowTransformer<String, PlanSourceResult> {
    override fun transformFlow(upstream: Flow<String>): Flow<PlanSourceResult> =
        upstream.flatMapLatest { path ->
            config(path).combine(markdownSource(path)) { configResult, markdownResult ->
                toResult(configResult, markdownResult)
            }
        }

    private fun config(path: String) =
        flowOf(path)
            .transform(configReadSource)
            .transform(configMergeSource)
            .flatMapLatest { mergeResult ->
                bakeConfig(mergeResult, path)
            }

    private fun bakeConfig(mergeResult: FlowResult<RawTixConfiguration>, path: String) =
        if (mergeResult.isSuccess) {
            val rawConfig = mergeResult.getOrThrow()
            flowOf(AuthConfigAction(path, rawConfig))
                .transform(authConfigUseCase)
                .map { ConfigBakerAction(rawConfig, it) }
                .transform(configBakerUseCase)
        } else {
            flowOf(FlowResult.failure(mergeResult.throwable))
        }

    private fun markdownSource(path: String) =
        flowOf(path).transform(markdownSource)

    private fun toResult(configResult: FlowResult<TixConfiguration>, markdownResult: FlowResult<String>) =
        when {
            configResult.isSuccess && markdownResult.isSuccess ->
                PlanSourceResult.Success(configResult.getOrThrow(), markdownResult.getOrThrow())
            configResult.isFailure -> PlanSourceResult.Error(configResult.toTixError())
            else -> PlanSourceResult.Error(markdownResult.toTixError())
        }
}

sealed class PlanSourceResult {
    data class Error(val error: TixError) : PlanSourceResult()
    data class Success(val configuration: TixConfiguration, val markdown: String) : PlanSourceResult()
}

fun planSourceCombiner(
    authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    configReadSource: FlowTransformer<String, List<RawTixConfiguration>>,
    configMergeSource: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>,
    markdownSource: FlowTransformer<String, FlowResult<String>>,
) = PlanSourceCombiner(authConfigUseCase, configBakerUseCase, configReadSource, configMergeSource, markdownSource)