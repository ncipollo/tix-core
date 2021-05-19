package org.tix.feature.plan.parse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.tix.domain.FlowTransformer
import org.tix.model.ticket.Ticket

internal class TicketParserUseCase(
    private val ticketParser: TicketParser
) : FlowTransformer<String, Result<List<Ticket>>> {
    override fun transformFlow(upstream: Flow<String>): Flow<Result<List<Ticket>>> =
        upstream.transform {
            val result = try {
                val ticket = ticketParser.parse(it)
                Result.success(ticket)
            } catch (exception: ParseException) {
                Result.failure(exception)
            }
            emit(result)
        }
}