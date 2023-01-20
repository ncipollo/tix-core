package org.tix.feature.plan.presentation

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import org.tix.feature.plan.domain.combiner.MarkdownPlanAction
import org.tix.feature.plan.domain.state.PlanDomainCompleted
import org.tix.feature.plan.domain.state.PlanDomainParsing
import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.presentation.reducer.PlanViewStateReducer
import org.tix.feature.plan.presentation.state.PlanViewState
import org.tix.test.testTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class PlanViewModelTest {
    private companion object {
        const val PATH = "path"
    }

    private val markdownEvent = PlanViewEvent.PlanUsingMarkdown(path = PATH, shouldDryRun = false)
    private val dryRunMarkdownEvent = PlanViewEvent.PlanUsingMarkdown(path = PATH, shouldDryRun = true)

    private val combinerUseCase = testTransformer(
        MarkdownPlanAction(PATH, false) to PlanDomainParsing(PATH),
        MarkdownPlanAction(PATH, true) to PlanDomainCompleted()
    )
    private val viewStateReducer = TestPlanViewStateReducer()

    @Test
    fun viewState_withDryRunMarkdownPlan() = runTest {
        val viewModel = PlanViewModel(combinerUseCase, this, viewStateReducer)

        viewModel.sendViewEvent(dryRunMarkdownEvent)

        viewModel.viewState.test {
            assertEquals(TestPlanViewState(PlanDomainCompleted()), awaitItem())
        }
    }

    @Test
    fun viewState_withMarkdownPlan() = runTest {
        val viewModel = PlanViewModel(combinerUseCase, this, viewStateReducer)

        viewModel.sendViewEvent(markdownEvent)

        viewModel.viewState.test {
            assertEquals(TestPlanViewState(PlanDomainParsing(PATH)), awaitItem())
        }
    }

    private data class TestPlanViewState(val domainState: PlanDomainState): PlanViewState {
        override val isComplete = true
    }

    private class TestPlanViewStateReducer: PlanViewStateReducer<TestPlanViewState> {
        override suspend fun reduce(domainState: PlanDomainState) = TestPlanViewState(domainState)
    }
}