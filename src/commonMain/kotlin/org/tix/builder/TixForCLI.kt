package org.tix.builder

import org.tix.Tix
import org.tix.config.domain.AuthConfigurationUseCase
import org.tix.config.domain.ConfigurationBakerUseCase
import org.tix.config.domain.ConfigurationMergeUseCase
import org.tix.config.domain.ConfigurationReadUseCase
import org.tix.config.reader.ConfigurationFileReader
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.config.reader.auth.AuthReader
import org.tix.config.reader.auth.FileAuthSourceReader
import org.tix.feature.plan.TixPlan
import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserUseCase
import org.tix.feature.plan.domain.ticket.TicketPlannerUseCase
import org.tix.feature.plan.domain.ticket.ticketPlannerFactory
import org.tix.platform.io.TextFileIO
import org.tix.platform.io.domain.TextFileUseCase

fun tixForCLI() = Tix(plan = cliPlan())

private fun cliPlan() = planWithFileSystem()

private fun planWithFileSystem() =
    TixPlan(
        authConfigUseCase = AuthConfigurationUseCase(authReader()),
        configBakerUseCase = ConfigurationBakerUseCase(),
        configReadSource = configReadSource(),
        configMergeSource = ConfigurationMergeUseCase(),
        markdownSource = TextFileUseCase(TextFileIO()),
        parserUseCase = TicketParserUseCase(TicketParser()),
        ticketPlannerUseCase = TicketPlannerUseCase(ticketPlannerFactory())
    )

private fun authReader() = AuthReader(FileAuthSourceReader(configFileReader()))

private fun configReadSource() = ConfigurationReadUseCase(RawTixConfigurationReader(configFileReader()))

private fun configFileReader() = ConfigurationFileReader(TextFileIO())