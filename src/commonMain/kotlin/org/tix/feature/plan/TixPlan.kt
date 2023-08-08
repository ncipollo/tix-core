package org.tix.feature.plan

import kotlinx.coroutines.CoroutineScope
import org.tix.config.data.TixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.feature.plan.domain.combiner.MarkdownPlanDomainCombiner
import org.tix.feature.plan.domain.parse.MarkdownSourceValidator
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.ticket.TicketPlanStatus
import org.tix.feature.plan.domain.ticket.TicketPlannerAction
import org.tix.feature.plan.presentation.PlanViewModel
import org.tix.feature.plan.presentation.planSourceCombiner
import org.tix.feature.plan.presentation.reducer.PlanViewStateReducer
import org.tix.feature.plan.presentation.state.PlanViewState
import org.tix.ticket.Ticket

class TixPlan<VS : PlanViewState> internal constructor(
    configUseCase: FlowTransformer<ConfigurationSourceOptions, FlowResult<TixConfiguration>>,
    markdownFileUseCase: FlowTransformer<String, FlowResult<String>>,
    parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>,
    ticketPlannerUseCase: FlowTransformer<TicketPlannerAction, TicketPlanStatus>,
    private val viewStateReducer: PlanViewStateReducer<VS>,
) {
    private val markdownPlanCombiner = MarkdownPlanDomainCombiner(
        planSourceCombiner = planSourceCombiner(
            configUseCase = configUseCase,
            markdownFileUseCase,
            MarkdownSourceValidator()
        ),
        parserUseCase = parserUseCase,
        plannerUseCase = ticketPlannerUseCase
    )

    fun planViewModel(planScope: CoroutineScope) =
        PlanViewModel(
            markdownPlanCombiner = markdownPlanCombiner,
            planScope = planScope,
            viewStateReducer = viewStateReducer
        )
}