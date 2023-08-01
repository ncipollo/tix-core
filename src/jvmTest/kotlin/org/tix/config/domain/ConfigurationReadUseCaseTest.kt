package org.tix.config.domain

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.reader.RootConfigurationReader
import org.tix.config.domain.reader.SavedConfigurationReader
import org.tix.config.domain.reader.WorkspaceConfigurationReader
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.domain.transform
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationReadUseCaseTest {
    private companion object {
        const val PATH_1 = "/path1/tix.md"
        val CONFIG_OPTIONS = ConfigurationSourceOptions.forMarkdownSource(PATH_1)
    }

    private val configPaths = ConfigurationPaths()
    private val rootConfig = RawTixConfiguration(variables = mapOf("root" to "config"))
    private val savedConfig = RawTixConfiguration(variables = mapOf("saved" to "config"))
    private val workspaceConfig = RawTixConfiguration(
        include = DynamicElement("saved"),
        variables = mapOf("workspace" to "workspace")
    )

    private val rootConfigReader = mockk<RootConfigurationReader>()
    private val savedConfigReader = mockk<SavedConfigurationReader>()
    private val workflowConfigReader = mockk<WorkspaceConfigurationReader>()

    private val reader = mockk<RawTixConfigurationReader>()
    private val useCase = ConfigurationReadUseCase(
        configPaths = configPaths,
        reader = reader,
        rootConfigReader = rootConfigReader,
        savedConfigReader = savedConfigReader,
        workspaceConfigReader = workflowConfigReader
    )

    @Test
    fun transformFlow_configurations() = runTest {
        val source = flowOf(CONFIG_OPTIONS).transform(useCase)
        respondWithConfigurations()
        source.test {
            assertEquals(listOf(rootConfig, savedConfig, workspaceConfig), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_noConfigurations() = runTest {
        val source = flowOf(CONFIG_OPTIONS).transform(useCase)
        respondWithNoConfigurations()
        source.test {
            assertEquals(emptyList(), awaitItem())
            awaitComplete()
        }
    }

    private fun respondWithConfigurations() {
        every { rootConfigReader.readRootConfig(CONFIG_OPTIONS) } returns rootConfig
        every { savedConfigReader.readSavedConfigs(CONFIG_OPTIONS, workspaceConfig) } returns listOf(savedConfig)
        every { workflowConfigReader.readWorkspaceConfig(CONFIG_OPTIONS) } returns workspaceConfig
    }

    private fun respondWithNoConfigurations() {
        every { rootConfigReader.readRootConfig(CONFIG_OPTIONS) } returns null
        every { savedConfigReader.readSavedConfigs(CONFIG_OPTIONS, null) } returns emptyList()
        every { workflowConfigReader.readWorkspaceConfig(CONFIG_OPTIONS) } returns null
    }
}