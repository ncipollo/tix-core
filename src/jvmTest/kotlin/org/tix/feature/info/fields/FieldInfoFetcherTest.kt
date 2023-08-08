package org.tix.feature.info.fields

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.domain.FlowResult
import org.tix.fixture.config.tixConfiguration
import org.tix.integrations.jira.JiraApi
import org.tix.integrations.jira.JiraApiFactory
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.field.FieldApi
import org.tix.test.testTransformer
import kotlin.test.Test
import kotlin.test.expect

class FieldInfoFetcherTest {
    private companion object {
        val CONFIG_OPTIONS = ConfigurationSourceOptions(savedConfigName = "config")
        val CONFIG_ERROR = RuntimeException("config_error")
        val JIRA_ERROR = RuntimeException("jira_error")
        val FIELDS = listOf(Field(id = "1", name = "field_1"), Field(id = "2", name = "field_2"))
    }

    private val fieldApi = mockk<FieldApi>()
    private val jiraApi = mockk<JiraApi> {
        every { field } returns fieldApi
    }
    private val jiraApiFactory = mockk<JiraApiFactory> {
        every { api(tixConfiguration.jira!!) } returns jiraApi
    }

    @Test
    fun fetchFields_failure_apiFailure() = runTest {
        val fetcher = FieldInfoFetcher(
            configUseCase = testTransformer(CONFIG_OPTIONS to FlowResult.success(tixConfiguration)),
            jiraApiFactory = jiraApiFactory
        )
        coEvery { fieldApi.fields() } throws JIRA_ERROR

        expect(JIRA_ERROR) {
            fetcher.fetchFields(CONFIG_OPTIONS).exceptionOrNull()!!
        }
    }

    @Test
    fun fetchFields_failure_configurationFailure() = runTest {
        val fetcher = FieldInfoFetcher(
            configUseCase = testTransformer(CONFIG_OPTIONS to FlowResult.failure(CONFIG_ERROR)),
            jiraApiFactory = jiraApiFactory
        )
        coEvery { fieldApi.fields() } returns FIELDS

        expect(CONFIG_ERROR) {
            fetcher.fetchFields(CONFIG_OPTIONS).exceptionOrNull()!!
        }
    }

    @Test
    fun fetchFields_success() = runTest {
        val fetcher = FieldInfoFetcher(
            configUseCase = testTransformer(CONFIG_OPTIONS to FlowResult.success(tixConfiguration)),
            jiraApiFactory = jiraApiFactory
        )
        coEvery { fieldApi.fields() } returns FIELDS

        expect(FIELDS) {
            fetcher.fetchFields(CONFIG_OPTIONS).getOrThrow()
        }
    }

}