package org.tix.feature.plan

import org.tix.domain.FlowTransformer
import org.tix.feature.plan.presentation.PlanViewModel
import org.tix.model.ticket.Ticket

class TixPlan internal constructor(
    private val markdownSource: FlowTransformer<String, Result<String>>,
    private val parserUseCase: FlowTransformer<String, Result<List<Ticket>>>
) {
    fun planViewModel() = PlanViewModel(markdownSource, parserUseCase)
}