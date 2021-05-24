package org.tix.feature.plan.domain.parse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.tix.domain.FlowTransformer
import org.tix.ext.checkExpectedError
import org.tix.model.ticket.Ticket

internal class TicketParserUseCase(
    private val ticketParser: TicketParser
) : FlowTransformer<String, Result<List<Ticket>>> {
    override fun transformFlow(upstream: Flow<String>): Flow<Result<List<Ticket>>> =
        upstream.transform {
            val result = kotlin.runCatching { ticketParser.parse(it) }
            result.checkExpectedError<ParseException>()
            emit(result)
        }
}