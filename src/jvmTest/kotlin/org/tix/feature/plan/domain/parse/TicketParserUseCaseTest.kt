package org.tix.feature.plan.domain.parse

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.intellij.markdown.MarkdownElementTypes
import org.intellij.markdown.ast.LeafASTNode
import org.tix.domain.transform
import org.tix.model.ticket.Ticket
import org.tix.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class TicketParserUseCaseTest {
    private companion object {
        const val MARKDOWN = "markdown"

        val ARGUMENTS = TicketParserArguments(markdown = "markdown")
        val ERROR = ParseException(
            message = "error",
            node = LeafASTNode(MarkdownElementTypes.BLOCK_QUOTE, 0, 1),
            markdownText = MARKDOWN
        )
    }

    private val ticket = Ticket(title = "ticket")
    private val ticketParser = mockk<TicketParser>()
    private val upstream = flow { emit(ARGUMENTS) }
    private val useCase = TicketParserUseCase(ticketParser)

    private val source = upstream.transform(useCase)

    @Test
    fun transformFlow_emitsErrorOnFailure() = runBlockingTest {
        every { ticketParser.parse(ARGUMENTS) } throws ERROR
        source.test {
            assertEquals(ERROR, expectItem().exceptionOrNull())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_emitsTicketOnSuccess() = runBlockingTest {
        every { ticketParser.parse(ARGUMENTS) } returns listOf(ticket)
        source.test {
            assertEquals(listOf(ticket), expectItem().getOrThrow())
            expectComplete()
        }
    }
}