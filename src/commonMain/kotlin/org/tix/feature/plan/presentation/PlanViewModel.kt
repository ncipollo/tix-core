package org.tix.feature.plan.presentation

import kotlinx.coroutines.flow.*
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.model.ticket.Ticket
import org.tix.presentation.TixViewModel

class PlanViewModel(
    private val markdownSource: FlowTransformer<String, Result<String>>,
    private val parserUseCase: FlowTransformer<String, Result<List<Ticket>>>
) : TixViewModel() {
    private val viewEvents = eventChannel<PlanViewEvent>()

    val viewState =
        viewEvents.asFlow()
            .flatMapLatest { routeViewEvent(it) }

    private fun routeViewEvent(event: PlanViewEvent) =
        when (event) {
            is PlanViewEvent.PlanUsingMarkdown -> markdownPlanning(event)
        }

    private fun markdownPlanning(event: PlanViewEvent.PlanUsingMarkdown) =
        flowOf(event.path)
            .transform(markdownSource)
            .filter { it.isSuccess }
            .map { it.getOrThrow() }
            .transform(parserUseCase)
            .map { PlanViewState() }
}