package org.tix.config.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.config.bake.validation.ConfigValidationException
import org.tix.config.data.TixConfiguration
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.domain.transform
import org.tix.fixture.config.rawTixConfiguration
import org.tix.fixture.config.ticketSystemAuth
import org.tix.fixture.config.tixConfiguration
import org.tix.serialize.dynamic.emptyDynamic
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationBakerUseCaseTest {
    private val useCase = ConfigurationBakerUseCase()

    @Test
    fun transformFlow_whenConfigurationsIsEmpty_emitsConfig() = runTest {
        flowOf(ConfigBakerAction(RawTixConfiguration(), ticketSystemAuth))
            .transform(useCase)
            .test {
                val expected = TixConfiguration(
                    include = emptyDynamic(),
                    github = null,
                    jira = null,
                    matrix = emptyMap(),
                    variables = emptyMap(),
                    variableToken = "$"
                )
                assertEquals(expected, awaitItem().getOrThrow())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_whenConfigurationsAreValid_emitsConfig() = runTest {
        flowOf(ConfigBakerAction(rawTixConfiguration, ticketSystemAuth))
            .transform(useCase)
            .test {
                assertEquals(tixConfiguration, awaitItem().getOrThrow())
                awaitComplete()
            }
    }

    @Test
    fun transformFlow_whenConfigurationsAreNotValid_emitsError() = runTest {
        val config = rawTixConfiguration.copy(github = RawGithubConfiguration())
        flowOf(ConfigBakerAction(config, ticketSystemAuth))
            .transform(useCase)
            .test {
                val expectedError = ConfigValidationException("Github Configuration", listOf("owner", "repo"))
                assertEquals(expectedError, awaitItem().exceptionOrNull())
                awaitComplete()
            }
    }
}