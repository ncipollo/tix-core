package org.tix.builder

import org.tix.Tix
import org.tix.config.domain.ConfigurationMergeUseCase
import org.tix.config.domain.ConfigurationReadUseCase
import org.tix.config.domain.ConfigurationReader
import org.tix.feature.plan.TixPlan
import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserUseCase
import org.tix.platform.io.TextFileIO
import org.tix.platform.io.domain.TextFileUseCase

actual fun tixForCLI() = Tix(plan = cliPlan())

private fun cliPlan() =
    TixPlan(
        configReadSource = ConfigurationReadUseCase(ConfigurationReader(TextFileIO())),
        configMergeSource = ConfigurationMergeUseCase(),
        markdownSource = TextFileUseCase(TextFileIO()),
        parserUseCase = TicketParserUseCase(TicketParser())
    )