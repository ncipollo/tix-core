package org.tix.feature.plan.domain.parse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.checkExpectedError
import org.tix.ext.toFlowResult
import org.tix.model.ticket.Ticket

internal class TicketParserUseCase(
    private val ticketParser: TicketParser
) : FlowTransformer<TicketParserArguments, FlowResult<List<Ticket>>> {
    override fun transformFlow(upstream: Flow<TicketParserArguments>): Flow<FlowResult<List<Ticket>>> =
        upstream.transform {
            val result = kotlin.runCatching { ticketParser.parse(it) }.toFlowResult()
            result.checkExpectedError<ParseException>()
            emit(result)
        }
}