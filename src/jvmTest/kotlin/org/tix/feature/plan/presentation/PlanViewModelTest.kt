package org.tix.feature.plan.presentation

import io.mockk.mockk
import org.tix.domain.FlowTransformer
import org.tix.model.ticket.Ticket

class PlanViewModelTest {
    private val markdownSource = mockk<FlowTransformer<String, Result<String>>>()
    private val parserUseCase = mockk<FlowTransformer<String, Result<List<Ticket>>>>()
    private val viewModel = PlanViewModel(markdownSource, parserUseCase)
}