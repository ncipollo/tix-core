package org.tix.builder

import org.tix.Tix
import org.tix.feature.plan.TixPlan
import org.tix.feature.plan.TixPlanSubmit
import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserUseCase
import org.tix.platform.io.TextFileIO
import org.tix.platform.io.domain.TextFileUseCase

fun tixForCLI() =
    Tix(
        plan = TixPlan(
            submitter = TixPlanSubmit(
                markdownSource = TextFileUseCase(TextFileIO()),
                parserUseCase = TicketParserUseCase(TicketParser())
            )
        )
    )