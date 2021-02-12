package org.tix.domain.plan.parse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.tix.model.ticket.Ticket

class TicketParserUseCase {
    fun parse() : Flow<Result<Ticket>> {
        return flow {  }
    }
}