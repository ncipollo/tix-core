package org.tix.feature.plan.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.ticket.TicketPlanStatus
import org.tix.feature.plan.domain.ticket.TicketPlannerAction
import org.tix.presentation.TixViewModel
import org.tix.ticket.Ticket

class PlanViewModel(
    private val planSourceCombiner: PlanSourceCombiner,
    private val parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>,
    private val planScope: CoroutineScope,
    private val plannerUseCase: FlowTransformer<TicketPlannerAction, TicketPlanStatus>
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
        flowOf(event.path)
            .transform(planSourceCombiner)
            .filterIsInstance<PlanSourceResult.Success>()
            .flatMapLatest { source ->
                flowOf(TicketParserArguments(markdown = source.markdown, configuration = source.configuration))
                    .transform(parserUseCase)
                    .map { TicketPlannerAction(source.configuration, shouldDryRun = true, it.getOrThrow()) }
                    .transform(plannerUseCase)
                    .map { PlanViewState(complete = true) }
            }
}