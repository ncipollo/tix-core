package org.tix.feature.plan.domain.combiner

import kotlinx.coroutines.flow.*
import org.tix.config.data.TixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.error.toTixError
import org.tix.feature.plan.domain.parse.TicketParserArguments
import org.tix.feature.plan.domain.state.PlanDomainError
import org.tix.feature.plan.domain.state.PlanDomainParsing
import org.tix.feature.plan.domain.state.PlanDomainState
import org.tix.feature.plan.domain.ticket.TicketPlanStatus
import org.tix.feature.plan.domain.ticket.TicketPlannerAction
import org.tix.feature.plan.presentation.PlanSourceResult
import org.tix.ticket.Ticket

class MarkdownPlanDomainCombiner(
    private val planSourceCombiner: FlowTransformer<MarkdownPlanAction, PlanSourceResult>,
    private val parserUseCase: FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>>,
    private val plannerUseCase: FlowTransformer<TicketPlannerAction, TicketPlanStatus>
) : FlowTransformer<MarkdownPlanAction, PlanDomainState> {
    override fun transformFlow(upstream: Flow<MarkdownPlanAction>): Flow<PlanDomainState> =
        upstream.flatMapLatest { action ->
            flowOf(action)
                .transform(planSourceCombiner)
                .flatMapLatest {
                    when (it) {
                        is PlanSourceResult.Error -> flowOf(PlanDomainError(it.error))
                        is PlanSourceResult.Success -> it.parseSource(action.shouldDryRun)
                    }
                }
                .onStart { emit(PlanDomainParsing(action.markdownSource)) }
        }

    private fun PlanSourceResult.Success.parseSource(shouldDryRun: Boolean) =
        flowOf(TicketParserArguments(markdown = markdown, configuration = configuration))
            .transform(parserUseCase)
            .flatMapLatest {
                when (it) {
                    is FlowResult.Failure -> flowOf(PlanDomainError(it.toTixError()))
                    is FlowResult.Success -> it.getOrThrow().plan(configuration, shouldDryRun = shouldDryRun)
                }
            }

    private fun List<Ticket>.plan(configuration: TixConfiguration, shouldDryRun: Boolean): Flow<PlanDomainState> =
        actionFlow(configuration, shouldDryRun)
            .transform(plannerUseCase)
            .map { PlanStatusMapper.mapStatus(it) }

    private fun List<Ticket>.actionFlow(configuration: TixConfiguration, shouldDryRun: Boolean) =
        flowOf(
            TicketPlannerAction(
                configuration,
                shouldDryRun = shouldDryRun,
                this
            )
        )
}

