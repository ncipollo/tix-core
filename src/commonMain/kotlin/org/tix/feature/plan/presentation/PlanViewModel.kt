package org.tix.feature.plan.presentation

import kotlinx.coroutines.flow.*
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.model.ticket.Ticket
import org.tix.presentation.TixViewModel

class PlanViewModel(
    private val planSourceCombiner: PlanSourceCombiner,
    private val parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>
) : TixViewModel() {
    private val events = MutableStateFlow<PlanViewEvent?>(null)

    val viewState =
        events.asStateFlow()
            .filterNotNull()
            .flatMapLatest { routeViewEvent(it) }

    fun sendViewEvent(event: PlanViewEvent) {
        events.value = event
    }

    private fun routeViewEvent(event: PlanViewEvent) =
        when (event) {
            is PlanViewEvent.PlanUsingMarkdown -> markdownPlanning(event)
        }

    private fun markdownPlanning(event: PlanViewEvent.PlanUsingMarkdown) =
        flowOf(event.path)
            .transform(planSourceCombiner)
            .filterIsInstance<PlanSourceResult.Success>()
            .map { TicketParserArguments(markdown = it.markdown, configuration = it.configuration) }
            .transform(parserUseCase)
            .map { PlanViewState(complete = true) }
}