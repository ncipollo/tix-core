package org.tix.feature.plan

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Ignore
import org.junit.Test
import org.tix.builder.tixPlanForCLI
import org.tix.feature.plan.presentation.PlanViewEvent

class TixPlanIntegrationTest {
    @Ignore
    @Test
    fun plan() = runTest {
        val viewModel = tixPlanForCLI().planViewModel(this)

        coroutineScope {
            var collectJob: Job? = null
            collectJob = launch {
                viewModel.viewState.collect {
                    println("state: #${it}")
                    if (it.isComplete) {
                        collectJob?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown("~/Desktop/tix/story.md", shouldDryRun = false))
            }
        }
    }
}