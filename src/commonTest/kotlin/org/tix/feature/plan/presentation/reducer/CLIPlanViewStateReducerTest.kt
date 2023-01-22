package org.tix.feature.plan.presentation.reducer

import kotlinx.coroutines.test.runTest
import org.tix.error.TixError
import org.tix.feature.plan.domain.state.*
import org.tix.feature.plan.domain.ticket.jira.JiraPlanResult
import org.tix.feature.plan.presentation.state.CLIPlanViewState
import kotlin.test.Test
import kotlin.test.expect

class CLIPlanViewStateReducerTest {
    private val viewStateReducer = CLIPlanViewStateReducer()

    @Test
    fun reduce_completed() = runTest {
        val domainState = PlanDomainCompleted()

        val expected = CLIPlanViewState(
            isComplete = true,
            message = "tix finished successfully 🎉"
        )
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_error() = runTest {
        val error = TixError()
        val domainState = PlanDomainError(TixError())

        val expected = CLIPlanViewState(message = "\n\n${error.message} ${error.mood}")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_parsing() = runTest {
        val path = "/tix.md"
        val domainState = PlanDomainParsing(path)

        val expected = CLIPlanViewState(message = "parsing tix.md 📕")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_planningStarted() = runTest {
        val domainState = PlanDomainStartingTicketCreation

        val expected = CLIPlanViewState(message = "processing tix 🎟️💨")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_update_level0() = runTest {
        val currentResult = JiraPlanResult(key = "TIX", level = 0, description = "Description")
        val domainState = PlanDomainUpdate(currentResult)

        val expected = CLIPlanViewState(message = "- TIX Description ✅")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_update_level1() = runTest {
        val currentResult = JiraPlanResult(key = "TIX", level = 1, description = "Description")
        val domainState = PlanDomainUpdate(currentResult)

        val expected = CLIPlanViewState(message = "-- TIX Description ✅")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }

    @Test
    fun reduce_update_level1_startingLevel() = runTest {
        val currentResult = JiraPlanResult(key = "TIX", level = 1, description = "Description", startingLevel = 1)
        val domainState = PlanDomainUpdate(currentResult)

        val expected = CLIPlanViewState(message = "- TIX Description ✅")
        expect(expected) {
            viewStateReducer.reduce(domainState)
        }
    }
}