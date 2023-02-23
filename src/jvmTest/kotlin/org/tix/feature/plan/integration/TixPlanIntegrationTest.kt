package org.tix.feature.plan.integration

import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.tix.builder.tixPlanForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import kotlin.test.Test

class TixPlanIntegrationTest {
    private val path = this::class.java.classLoader.getResource("plan/integration/test.md")!!.path

    @Test
    fun plan() = runTest {
        val viewModel = tixPlanForCLI().planViewModel(this)

        coroutineScope {
            var collectJob: Job? = null
            collectJob = launch {
                viewModel.viewState.collect {
                    println("state: $it")
                    if (it.isComplete) {
                        collectJob?.cancel()
                    }
                }
            }
            launch {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown(path, shouldDryRun = true))
            }
        }
    }
}