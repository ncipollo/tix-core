package org.tix.config.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.domain.FlowResult
import org.tix.domain.FlowTransformer
import org.tix.domain.transform
import org.tix.fixture.config.rawTixConfiguration
import org.tix.fixture.config.ticketSystemAuth
import org.tix.fixture.config.tixConfiguration
import org.tix.test.testTransformer
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationUseCaseTest {
    private companion object {
        val CONFIG_LIST = listOf(rawTixConfiguration)
        val ERROR = RuntimeException("oh noes")
        const val PATH = "a/path.md"

        val BAKER_ACTION = ConfigBakerAction(rawTixConfiguration, ticketSystemAuth)
        val CONFIG_OPTIONS = ConfigurationSourceOptions.forMarkdownSource(PATH)
        val AUTH_ACTION = AuthConfigAction(CONFIG_OPTIONS.workspaceDirectory, rawTixConfiguration)
    }

    private val configSuccessfulReadSource = testTransformer(CONFIG_OPTIONS to CONFIG_LIST)
    private val source = flowOf(ConfigurationSourceOptions.forMarkdownSource(PATH))

    @Test
    fun transform_bakerFailure() = runTest {
        val useCase = configUseCase(configBakerUseCase = configBakerError())
        source.transform(useCase)
            .test {
                assertEquals(FlowResult.failure(ERROR), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transform_mergeFailure() = runTest {
        val useCase = configUseCase(configMergeUseCase = configMergeErrorUseCase())
        source.transform(useCase)
            .test {
                assertEquals(FlowResult.failure(ERROR), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun transform_successfulConfiguration() = runTest {
        val useCase = configUseCase()
        source.transform(useCase)
            .test {
                assertEquals(FlowResult.success(tixConfiguration), awaitItem())
                awaitComplete()
            }
    }


    private fun configUseCase(
        authReadSource: FlowTransformer<AuthConfigAction, TicketSystemAuth> = authConfigUseCase(),
        configReadUseCase: FlowTransformer<ConfigurationSourceOptions, List<RawTixConfiguration>> = configSuccessfulReadSource,
        configBakerUseCase: FlowTransformer<ConfigBakerAction, FlowResult<TixConfiguration>> = configBakerSuccess(),
        configMergeUseCase: FlowTransformer<List<RawTixConfiguration>, FlowResult<RawTixConfiguration>> = configMergeSuccessUseCase(),
    ) = ConfigurationUseCase(authReadSource, configBakerUseCase, configReadUseCase, configMergeUseCase)

    private fun authConfigUseCase() = testTransformer(AUTH_ACTION to ticketSystemAuth)

    private fun configBakerError() =
        testTransformer(BAKER_ACTION to FlowResult.failure<TixConfiguration>(ERROR))

    private fun configBakerSuccess() = testTransformer(BAKER_ACTION to FlowResult.success(tixConfiguration))

    private fun configMergeErrorUseCase() =
        testTransformer(CONFIG_LIST to FlowResult.failure<RawTixConfiguration>(ERROR))

    private fun configMergeSuccessUseCase() =
        testTransformer(CONFIG_LIST to FlowResult.success(rawTixConfiguration))
}