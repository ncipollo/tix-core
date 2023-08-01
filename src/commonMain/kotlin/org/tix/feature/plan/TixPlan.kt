package org.tix.feature.plan

import kotlinx.coroutines.CoroutineScope
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.AuthConfigAction
import org.tix.config.domain.ConfigBakerAction
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.domain.TicketSystemAuth
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

class TixPlan<VS: PlanViewState> internal constructor(
    authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    configReadUseCase: FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>>,
    configMergeUseCase: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>,
    markdownFileUseCase: FlowTransformer<String, FlowResult<String>>,
    parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>,
    ticketPlannerUseCase: FlowTransformer<TicketPlannerAction, TicketPlanStatus>,
    private val viewStateReducer: PlanViewStateReducer<VS>,
) {
    private val markdownPlanCombiner = MarkdownPlanDomainCombiner(
        planSourceCombiner = planSourceCombiner(
            authConfigUseCase,
            configBakerUseCase,
            configReadUseCase,
            configMergeUseCase,
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