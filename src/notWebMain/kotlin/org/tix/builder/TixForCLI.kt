package org.tix.builder

import org.tix.Tix
import org.tix.feature.plan.TixPlan
import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserUseCase
import org.tix.platform.io.TextFileIO
import org.tix.platform.io.domain.TextFileUseCase

actual fun tixForCLI() = Tix(plan = cliPlan())

private fun cliPlan() =
    TixPlan(
        markdownSource = TextFileUseCase(TextFileIO()),
        parserUseCase = TicketParserUseCase(TicketParser())
    )