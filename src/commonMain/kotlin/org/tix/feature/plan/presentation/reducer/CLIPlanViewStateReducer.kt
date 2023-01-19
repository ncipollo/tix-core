package org.tix.feature.plan.presentation.reducer

import okio.Path.Companion.toPath
import org.tix.error.TixError
import org.tix.feature.plan.domain.state.*
import org.tix.feature.plan.domain.ticket.TicketPlanResult
import org.tix.feature.plan.presentation.state.CLIPlanViewState

class CLIPlanViewStateReducer : PlanViewStateReducer<CLIPlanViewState> {
    override suspend fun reduce(domainState: PlanDomainState) =
        when (domainState) {
            is PlanDomainCompleted -> completeState()
            is PlanDomainError -> errorState(domainState.ex)
            is PlanDomainParsing -> parsingState(domainState.path)
            PlanDomainStartingTicketCreation -> creatingTixState()
            is PlanDomainUpdate -> updatingTix(domainState.currentResult)
        }

    private fun completeState() = CLIPlanViewState(
        isComplete = true,
        message = "tix finished successfully 🎉"
    )

    private fun errorState(ex: TixError) = CLIPlanViewState(message = "\n\n${ex.message} ${ex.mood}")

    private fun parsingState(path: String) = CLIPlanViewState(message = "parsing ${path.filename()} 📕")

    private fun String.filename() = toPath().name


    private fun creatingTixState() = CLIPlanViewState(message = "processing tix 🎟️💨")

    private fun updatingTix(currentResult: TicketPlanResult) =
        CLIPlanViewState(
            message = "${levelPrefix(currentResult.level)} ${currentResult.key} ${currentResult.description} ✅"
        )

    private fun levelPrefix(level: Int) = "-".repeat(level)
}