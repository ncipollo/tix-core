package org.tix.feature.plan.presentation

import kotlinx.coroutines.flow.*
import org.tix.config.data.TixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.TixError
import org.tix.error.toTixError
import org.tix.ext.toFlowResult
import org.tix.feature.plan.domain.combiner.MarkdownPlanAction
import org.tix.feature.plan.domain.parse.MarkdownFileSource
import org.tix.feature.plan.domain.parse.MarkdownSource
import org.tix.feature.plan.domain.parse.MarkdownSourceValidator
import org.tix.feature.plan.domain.parse.MarkdownTextSource

class PlanSourceCombiner(
    private val configurationUseCase: FlowTransformer<ConfigurationSourceOptions, FlowResult<TixConfiguration>>,
    private val markdownFileUseCase: FlowTransformer<String, FlowResult<String>>,
    private val markdownValidator: MarkdownSourceValidator
) : FlowTransformer<MarkdownPlanAction, PlanSourceResult> {
    override fun transformFlow(upstream: Flow<MarkdownPlanAction>): Flow<PlanSourceResult> =
        upstream.flatMapLatest { action ->
            markdown(action.markdownSource)
                .flatMapLatest { markdownResult ->
                    val markdownContent = markdownResult.getOrNull()
                    val optionsWithMarkdown = action.configSourceOptions.copy(markdownContent = markdownContent)
                    flowOf(optionsWithMarkdown)
                        .transform(configurationUseCase)
                        .map { configResult -> toResult(configResult, markdownResult) }
                }.catch {
                    // Catch parsing exceptions from config or markdown
                    emit(PlanSourceResult.Error(it.toTixError()))
                }
        }

    private fun markdown(markdownInput: MarkdownSource): Flow<FlowResult<String>> {
        val result = runCatching {
            markdownValidator.validate(markdownInput)
            ""
        }
        if (result.isSuccess) {
            return readMarkdown(markdownInput)
        }
        return flowOf(result.toFlowResult())
    }

    private fun readMarkdown(markdownInput: MarkdownSource) =
        when (markdownInput) {
            is MarkdownFileSource -> flowOf(markdownInput.path).transform(markdownFileUseCase)
            is MarkdownTextSource -> flowOf(FlowResult.success(markdownInput.markdown))
        }

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
    configUseCase: FlowTransformer<ConfigurationSourceOptions, FlowResult<TixConfiguration>>,
    markdownFileUseCase: FlowTransformer<String, FlowResult<String>>,
    markdownValidator: MarkdownSourceValidator
) = PlanSourceCombiner(configUseCase, markdownFileUseCase, markdownValidator)