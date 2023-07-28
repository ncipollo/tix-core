package org.tix.feature.plan.presentation.reducer

import okio.Path.Companion.toPath
import org.tix.error.TixError
import org.tix.feature.plan.domain.state.*
import org.tix.feature.plan.domain.ticket.TicketPlanResult
import org.tix.feature.plan.presentation.state.CLIPlanViewState

class CLIPlanViewStateReducer : PlanViewStateReducer<CLIPlanViewState> {
    override suspend fun reduce(domainState: PlanDomainState) =
        when (domainState) {
            is PlanDomainCompleted -> completeState(domainState)
            is PlanDomainError -> errorState(domainState.ex)
            is PlanDomainParsing -> parsingState(domainState.path)
            PlanDomainStartingTicketCreation -> creatingTixState()
            is PlanDomainUpdate -> updatingTix(domainState.currentResult)
        }

    private fun completeState(domainState: PlanDomainCompleted) =
        CLIPlanViewState(
            isComplete = true,
            message = completeMessage(domainState)
        )

    private fun completeMessage(domainState: PlanDomainCompleted) =
        if (domainState.info.wasDryRun) {
            "tix finished successfully 🎉\n${domainState.info.message}"
        } else {
            "tix finished successfully 🎉"
        }

    private fun errorState(ex: TixError) = CLIPlanViewState(
        isComplete = true,
        message = "\n\n${ex.message} ${ex.mood}${errorCauseMessage(ex)}"
    )

    private fun errorCauseMessage(ex: TixError) =
        ex.cause?.message?.let { "\nCause: $it" } ?: ""

    private fun parsingState(path: String) = CLIPlanViewState(message = "parsing ${path.filename()} 📕")

    private fun String.filename() = toPath().name


    private fun creatingTixState() = CLIPlanViewState(message = "processing tix 🎟️💨")

    private fun updatingTix(currentResult: TicketPlanResult) =
        CLIPlanViewState(message = updatingMessage(currentResult))

    private fun updatingMessage(currentResult: TicketPlanResult) =
        if (currentResult.wasDryRun) {
            currentResult.description
        } else {
            "${levelPrefix(currentResult.adjustedLevel())} ${currentResult.key} ${currentResult.description} ✅"
        }

    private fun levelPrefix(level: Int) = "-".repeat(level + 1)

    private fun TicketPlanResult.adjustedLevel() = level - startingLevel
}