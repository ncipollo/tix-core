package org.tix.feature.plan.domain.combiner

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.toTixError
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.state.PlanDomainError
import org.tix.feature.plan.domain.state.PlanDomainParsing
import org.tix.feature.plan.domain.state.PlanDomainStartingTicketCreation
import org.tix.feature.plan.domain.ticket.TicketPlanStarted
import org.tix.feature.plan.domain.ticket.TicketPlanStatus
import org.tix.feature.plan.domain.ticket.TicketPlannerAction
import org.tix.feature.plan.presentation.PlanSourceResult
import org.tix.fixture.config.tixConfiguration
import org.tix.test.testTransformer
import org.tix.ticket.Ticket
import kotlin.test.Test
import kotlin.test.assertEquals

class MarkdownPlanDomainCombinerTest {
    private companion object {
        val CONFIG = tixConfiguration
        const val MARKDOWN = "markdown"
        const val PATH = "path"
        val PARSER_ARGS = TicketParserArguments(MARKDOWN, CONFIG)
        val PARSER_ERROR = RuntimeException("parser error")
        val SOURCE_ERROR = RuntimeException("source fail").toTixError()
        val TICKETS = listOf(Ticket(tixId = "1"), Ticket(tixId = "2"))

        val MARKDOWN_ACTION = MarkdownPlanAction(PATH, false)
        val PLANNER_ACTION = TicketPlannerAction(CONFIG, false, TICKETS)
    }

    private val failedSource: FlowTransformer<String, PlanSourceResult> =
        testTransformer(PATH to PlanSourceResult.Error(SOURCE_ERROR))
    private val successfulSource: FlowTransformer<String, PlanSourceResult> =
        testTransformer(PATH to PlanSourceResult.Success(CONFIG, MARKDOWN))

    private val failedParser =
        testTransformer(PARSER_ARGS to FlowResult.failure<List<Ticket>>(PARSER_ERROR.toTixError()))
    private val successfulParser = testTransformer(PARSER_ARGS to FlowResult.success(TICKETS))

    private val successfulPlanner: FlowTransformer<TicketPlannerAction, TicketPlanStatus> =
        testTransformer(PLANNER_ACTION to TicketPlanStarted)

    @Test
    fun transform_failedParser() = runTest {
        flowOf(MARKDOWN_ACTION)
            .transform(MarkdownPlanDomainCombiner(successfulSource, failedParser, successfulPlanner))
            .test {
                assertEquals(PlanDomainParsing, awaitItem())
                assertEquals(PlanDomainError(PARSER_ERROR.toTixError()), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transform_failedSource() = runTest {
        flowOf(MARKDOWN_ACTION)
            .transform(MarkdownPlanDomainCombiner(failedSource, successfulParser, successfulPlanner))
            .test {
                assertEquals(PlanDomainParsing, awaitItem())
                assertEquals(PlanDomainError(SOURCE_ERROR), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transform_successful() = runTest {
        flowOf(MARKDOWN_ACTION)
            .transform(MarkdownPlanDomainCombiner(successfulSource, successfulParser, successfulPlanner))
            .test {
                assertEquals(PlanDomainParsing, awaitItem())
                assertEquals(PlanDomainStartingTicketCreation, awaitItem())
                awaitComplete()
            }
    }
}