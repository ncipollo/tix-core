package org.tix.feature.plan

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.junit.Ignore
import org.junit.Test
import org.tix.builder.tixForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import org.tix.test.runBlockingTest

class TixPlanIntegrationTest {
    @Ignore
    @Test
    fun plan() = runBlockingTest {
        val viewModel = tixForCLI().plan.planViewModel()

        coroutineScope {
            var collectJob: Job? = null
            collectJob = launch {
                viewModel.viewState.collect {
                    println("state: #${it}")
                    if (it.complete) {
                        collectJob?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown("~/Desktop/tix/story.md"))
            }
        }
    }
}