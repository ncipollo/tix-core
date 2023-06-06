package org.tix.feature.plan.integration

import app.cash.turbine.test
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.tix.builder.tixPlanForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import org.tix.feature.plan.presentation.state.CLIPlanViewState
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TixPlanIntegrationTest {
    private val path = this::class.java.classLoader.getResource("plan/integration/test.md")!!.path
    private val expectedCompletionMessage = """
        tix finished successfully 🎉
        Ticket Stats:
        - Total Tickets: 3
        - Epic: 1
        - Story: 1
        - Task: 1
    """.trimIndent()

    @Test
    fun plan() = runTest {
        val viewModel = tixPlanForCLI().planViewModel(this)

        coroutineScope {
            var collectJob: Job? = null
            collectJob = launch {
                viewModel.viewState
                    .onEach { println("$it") }
                    .test {

                        assertEquals(CLIPlanViewState(message = "parsing test.md 📕"), awaitItem())
                        assertEquals(CLIPlanViewState(message = "processing tix 🎟️💨"), awaitItem())
                        assertContains(awaitItem().message, "🚀 Test Epic")
                        assertContains(awaitItem().message, "🚀 Test Story")
                        assertContains(awaitItem().message, "🚀 Test Task")

                        assertEquals(
                            CLIPlanViewState(message = expectedCompletionMessage, isComplete = true),
                            awaitItem()
                        )
                        collectJob?.cancel()
                    }
            }

            launch {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown(path, shouldDryRun = true))
            }
        }
    }
}