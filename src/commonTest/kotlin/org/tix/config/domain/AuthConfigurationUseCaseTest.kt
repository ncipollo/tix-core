package org.tix.config.domain

import app.cash.turbine.test
import kotlinx.coroutines.flow.flowOf
import org.tix.config.data.auth.AuthConfiguration
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.config.data.raw.RawGithubConfiguration
import org.tix.config.data.raw.RawJiraConfiguration
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.reader.auth.AuthSourceReader
import org.tix.domain.transform
import org.tix.model.ticket.system.TicketSystemType
import org.tix.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthConfigurationUseCaseTest {
    private companion object {
        val GITHUB_AUTH_CONFIG = AuthConfiguration("github_user", "github_password")
        val JIRA_AUTH_CONFIG = AuthConfiguration("jira_user", "jira_password")
        val RAW_CONFIGURATION = RawTixConfiguration(
            github = RawGithubConfiguration(auth = RawAuthConfiguration(file = "github")),
            jira = RawJiraConfiguration(auth = RawAuthConfiguration(file = "jira")),
        )
    }

    @Test
    fun transform_whenConfigurationIsNotEmpty_readsAuthConfigurations() = runBlockingTest {
        val authReader = TestAuthReader(false)
        val useCase = AuthConfigurationUseCase(authReader)
        val source = flowOf(AuthConfigAction("", RAW_CONFIGURATION))

        source.transform(useCase)
            .test {
                val expected = TicketSystemAuth(GITHUB_AUTH_CONFIG, JIRA_AUTH_CONFIG)
                assertEquals(expected, expectItem())
                expectComplete()
            }
    }

    @Test
    fun transform_whenConfigurationIsEmpty_readsAuthConfigurations() = runBlockingTest {
        val authReader = TestAuthReader(true)
        val useCase = AuthConfigurationUseCase(authReader)
        val source = flowOf(AuthConfigAction("", RawTixConfiguration()))

        source.transform(useCase)
            .test {
                val expected = TicketSystemAuth(GITHUB_AUTH_CONFIG, JIRA_AUTH_CONFIG)
                assertEquals(expected, expectItem())
                expectComplete()
            }
    }

    private class TestAuthReader(private val emptyAuthConfigs: Boolean) : AuthSourceReader {
        override fun read(
            markdownPath: String,
            rawAuthConfig: RawAuthConfiguration,
            ticketSystemType: TicketSystemType
        ): AuthConfiguration {
            assertRawConfig(rawAuthConfig, ticketSystemType)
            return when (ticketSystemType) {
                TicketSystemType.GITHUB -> GITHUB_AUTH_CONFIG
                TicketSystemType.JIRA -> JIRA_AUTH_CONFIG
            }
        }

        private fun assertRawConfig(
            rawAuthConfig: RawAuthConfiguration,
            ticketSystemType: TicketSystemType
        ) {
            if (emptyAuthConfigs) {
                assertEquals(RawAuthConfiguration(), rawAuthConfig)
            } else {
                when (ticketSystemType) {
                    TicketSystemType.GITHUB -> assertEquals(RAW_CONFIGURATION.github!!.auth, rawAuthConfig)
                    TicketSystemType.JIRA -> assertEquals(RAW_CONFIGURATION.jira!!.auth, rawAuthConfig)
                }
            }
        }
    }
}