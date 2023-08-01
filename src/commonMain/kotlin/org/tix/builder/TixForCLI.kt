package org.tix.builder

import org.tix.config.ConfigurationPaths
import org.tix.config.domain.AuthConfigurationUseCase
import org.tix.config.domain.ConfigurationBakerUseCase
import org.tix.config.domain.ConfigurationMergeUseCase
import org.tix.config.domain.ConfigurationReadUseCase
import org.tix.config.reader.ConfigurationFileReader
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.config.reader.auth.AuthConfigurationPaths
import org.tix.config.reader.auth.AuthReader
import org.tix.config.reader.auth.EnvAuthSourceReader
import org.tix.config.reader.auth.FileAuthSourceReader
import org.tix.feature.plan.TixPlan
import org.tix.feature.plan.domain.parse.TicketParser
import org.tix.feature.plan.domain.parse.TicketParserUseCase
import org.tix.feature.plan.domain.ticket.TicketPlannerUseCase
import org.tix.feature.plan.domain.ticket.ticketPlannerFactory
import org.tix.feature.plan.presentation.reducer.CLIPlanViewStateReducer
import org.tix.feature.plan.presentation.reducer.PlanViewStateReducer
import org.tix.feature.plan.presentation.state.PlanViewState
import org.tix.platform.PlatformEnv
import org.tix.platform.SandboxEnv
import org.tix.platform.io.TextFileIO
import org.tix.platform.io.domain.TextFileUseCase
import org.tix.platform.sanitizedEnv

fun tixPlanForCLI() = planWithFileSystem(CLIPlanViewStateReducer())

private fun <VS : PlanViewState> planWithFileSystem(viewStateReducer: PlanViewStateReducer<VS>) =
    TixPlan(
        authConfigUseCase = AuthConfigurationUseCase(authReader()),
        configBakerUseCase = ConfigurationBakerUseCase(),
        configReadUseCase = configReadSource(),
        configMergeUseCase = ConfigurationMergeUseCase(),
        markdownFileUseCase = TextFileUseCase(TextFileIO()),
        parserUseCase = TicketParserUseCase(TicketParser()),
        ticketPlannerUseCase = TicketPlannerUseCase(ticketPlannerFactory(cliEnv())),
        viewStateReducer = viewStateReducer

    )

private fun authReader() = AuthReader(
    fileReader = FileAuthSourceReader(AuthConfigurationPaths(), configFileReader()),
    envReader = EnvAuthSourceReader(authEnv())
)

private fun configReadSource() = ConfigurationReadUseCase(
    configPaths = ConfigurationPaths(),
    reader = RawTixConfigurationReader(configFileReader())
)

private fun configFileReader() = ConfigurationFileReader(TextFileIO())

private fun authEnv() = SandboxEnv(PlatformEnv) { true }
private fun cliEnv() = sanitizedEnv(PlatformEnv)