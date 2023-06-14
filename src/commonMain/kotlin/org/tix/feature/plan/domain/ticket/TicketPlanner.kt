package org.tix.feature.plan.domain.ticket

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import org.tix.config.data.TicketSystemConfiguration
import org.tix.feature.plan.domain.error.TicketPlanningException
import org.tix.feature.plan.domain.render.BodyRenderer
import org.tix.platform.Env
import org.tix.ticket.Ticket

class TicketPlanner<R : TicketPlanResult>(
    private val env: Env,
    private val renderer: BodyRenderer,
    private val system: TicketPlanningSystem<R>,
    private val systemConfig: TicketSystemConfiguration,
    private val variables: Map<String, String>,
    private val variableToken: String
) {
    private val workflowPlanner = WorkflowPlanner(env, system, systemConfig.workflows)

    fun plan(tickets: List<Ticket>): Flow<TicketPlanStatus> =
        flow {
            emit(TicketPlanStarted)

            try {
                var context = PlanningContext<R>(
                    config = systemConfig,
                    level = systemConfig.startingLevel,
                    startingLevel = systemConfig.startingLevel,
                    variables = variables,
                    variableToken = variableToken
                )
                system.validate(context, tickets)

                context = beforeAll(context)
                context = planTickets(context, tickets)
                afterAll(context)
                emit(TicketPlanCompleted(system.completeInfo()))
            } catch (e: TicketPlanningException) {
                emit(TicketPlanFailed(e))
            }
        }

    private suspend fun beforeAll(context: PlanningContext<R>,): PlanningContext<R> {
        system.setup(context)
        return workflowPlanner.beforeAll(context)
    }

    private suspend fun FlowCollector<TicketPlanStatus>.planTickets(
        context: PlanningContext<R>,
        tickets: List<Ticket>
    ): PlanningContext<R> {
        var currentContext = context
        tickets.forEach {
            currentContext = planTicket(currentContext, it)
        }
        return currentContext
    }

    private suspend fun FlowCollector<TicketPlanStatus>.planTicket(
        context: PlanningContext<R>,
        ticket: Ticket
    ): PlanningContext<R> {
        val beforeContext = workflowPlanner.beforeEach(context)
        val renderedTicket = TicketTransformer(context, env, renderer, ticket).ticket()
        val operation = PlanningOperationDecider(renderedTicket).operation()
        val result = system.planTicket(beforeContext, renderedTicket, operation)
        val resultContext = beforeContext.createResultContext(result)
        val afterContext = workflowPlanner.afterEach(resultContext)

        emit(TicketPlanUpdated(result))

        planTickets(afterContext.createChildContext(result), ticket.children)

        return afterContext
    }

    private suspend fun afterAll(context: PlanningContext<R>) {
        workflowPlanner.afterAll(context)
    }
}