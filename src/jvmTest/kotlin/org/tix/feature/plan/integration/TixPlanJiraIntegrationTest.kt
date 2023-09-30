package org.tix.feature.plan.integration

import app.cash.turbine.test
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.tix.builder.tixPlanForCLI
import org.tix.feature.plan.presentation.PlanViewEvent
import org.tix.feature.plan.presentation.state.CLIPlanViewState
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TixPlanJiraIntegrationTest {
    private val path = this::class.java.classLoader.getResource("plan/integration/jira/test.md")!!.path
    private val expectedCompletionMessage = """
        tix finished successfully 🎉
        Ticket Stats:
        - Total Tickets: 4
        - Epic: 1
        - Story: 1
        - Tasks: 2
    """.trimIndent()

    @Test
    fun plan() = runTest {
        val viewModel = tixPlanForCLI().planViewModel(this)

        viewModel.viewState
            .onEach { println(it) }
            .test {
                viewModel.sendViewEvent(PlanViewEvent.PlanUsingMarkdown(path, shouldDryRun = true))

                assertEquals(CLIPlanViewState(message = "parsing test.md 📕"), awaitItem())
                assertEquals(CLIPlanViewState(message = "processing tix 🎟️💨"), awaitItem())
                awaitItem().apply {
                    assertContains(message, "Test Epic")
                    assertContains(message, "should be empty - ( )")
                    assertContains(message, "labels = test")
                }
                awaitItem().apply {
                    assertContains(message, "Test Story")
                    assertContains(message, "tix_1")
                }
                assertContains(awaitItem().message, "android: Test Task")
                assertContains(awaitItem().message, "iOS: Test Task")

                assertEquals(
                    CLIPlanViewState(message = expectedCompletionMessage, isComplete = true),
                    awaitItem()
                )
            }

    }
}