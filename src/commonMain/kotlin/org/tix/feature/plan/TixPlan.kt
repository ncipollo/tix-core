package org.tix.feature.plan

import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.presentation.PlanViewModel
import org.tix.feature.plan.presentation.planSourceCombiner
import org.tix.model.ticket.Ticket

class TixPlan internal constructor(
    private val configReadSource: FlowTransformer<String, List<TixConfiguration>>,
    private val configMergeSource: FlowTransformer<List<TixConfiguration>, FlowResult<TixConfiguration>>,
    private val markdownSource: FlowTransformer<String, FlowResult<String>>,
    private val parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>
) {
    fun planViewModel() =
        PlanViewModel(
            planSourceCombiner(configReadSource, configMergeSource, markdownSource),
            parserUseCase
        )
}