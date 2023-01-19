package org.tix.feature.plan.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.feature.plan.domain.combiner.MarkdownPlanAction
import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.reducer.PlanViewStateReducer
import org.tix.feature.plan.presentation.state.PlanViewState
import org.tix.presentation.TixViewModel

class PlanViewModel<VS: PlanViewState>(
    private val markdownPlanCombiner: FlowTransformer<MarkdownPlanAction, PlanDomainState>,
    private val planScope: CoroutineScope,
    private val viewStateReducer: PlanViewStateReducer<VS>
) : TixViewModel() {
    private val events = MutableSharedFlow<PlanViewEvent>()

    val viewState =
        events.asSharedFlow()
            .flatMapLatest { routeViewEvent(it) }

    fun sendViewEvent(event: PlanViewEvent) {
        planScope.launch {
            events.emit(event)
        }
    }

    private fun routeViewEvent(event: PlanViewEvent) =
        when (event) {
            is PlanViewEvent.PlanUsingMarkdown -> markdownPlanning(event)
        }

    private fun markdownPlanning(event: PlanViewEvent.PlanUsingMarkdown) =
        flowOf(event.toAction())
            .transform(markdownPlanCombiner)
            .map { viewStateReducer.reduce(it) }

    private fun PlanViewEvent.PlanUsingMarkdown.toAction() = MarkdownPlanAction(path, shouldDryRun)
}