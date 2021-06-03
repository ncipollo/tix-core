package org.tix.feature.plan.presentation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.TixError
import org.tix.error.toTixError

class PlanSourceCombiner(
    private val configReadSource: FlowTransformer<String, List<TixConfiguration>>,
    private val configMergeSource: FlowTransformer<List<TixConfiguration>, FlowResult<TixConfiguration>>,
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
    configReadSource: FlowTransformer<String, List<TixConfiguration>>,
    configMergeSource: FlowTransformer<List<TixConfiguration>, FlowResult<TixConfiguration>>,
    markdownSource: FlowTransformer<String, FlowResult<String>>,
) = PlanSourceCombiner(configReadSource, configMergeSource, markdownSource)