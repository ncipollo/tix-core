package org.tix.feature.plan

import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.AuthConfigAction
import org.tix.config.domain.ConfigBakerAction
import org.tix.config.domain.TicketSystemAuth
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.presentation.PlanViewModel
import org.tix.feature.plan.presentation.planSourceCombiner
import org.tix.ticket.Ticket

class TixPlan internal constructor(
    private val authConfigUseCase: FlowTransformer<AuthConfigAction, TicketSystemAuth>,
    private val configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>>,
    private val configReadSource: FlowTransformer<String, List<RawTixConfiguration>>,
    private val configMergeSource: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>>,
    private val markdownSource: FlowTransformer<String, FlowResult<String>>,
    private val parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>
) {
    fun planViewModel() =
        PlanViewModel(
            planSourceCombiner(
                authConfigUseCase,
                configBakerUseCase,
                configReadSource,
                configMergeSource,
                markdownSource
            ),
            parserUseCase
        )
}