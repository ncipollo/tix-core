package org.tix.config.domain

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.domain.transform
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationReadUseCaseTest {
    private companion object {
        const val PATH1 = "/path1/tix.md"
        const val PATH2 = "/path2/tix.md"
    }

    private val rootConfig = RawTixConfiguration(variables = mapOf("root" to "config"))
    private val savedConfig = RawTixConfiguration(variables = mapOf("saved" to "config"))
    private val workSpaceConfig = RawTixConfiguration(
        include = DynamicElement("saved"),
        variables = mapOf("workspace" to "workspace")
    )

    private val reader = mockk<RawTixConfigurationReader>()
    private val useCase = ConfigurationReadUseCase(reader)

    @Test
    fun transformFlow_whenAllConfigsAreNull_returnsEmptyList() = runTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsNull()
        workspaceConfigReturnsNull()

        source.test {
            assertEquals(emptyList(), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenAllConfigsAreRead_returnsAllConfigs() = runTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        savedConfigReturnsValue()

        source.test {
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenMultiplePathsAreProvided_returnsMultipleConfigLists() = runTest {
        val source = flowOf(PATH1, PATH2).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        workspaceConfigReturnsValue(PATH2)
        savedConfigReturnsValue()

        source.test {
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), awaitItem())
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenSavedConfigIsNull_returnsListWithoutSavedConfig() = runTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        savedConfigReturnsNull()

        source.test {
            assertEquals(listOf(rootConfig, workSpaceConfig), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenWorkspaceConfigHasNoInclude_returnsListWithoutSavedConfig() = runTest {
        val workspaceConfigNoSaved = workSpaceConfig.copy(include = DynamicElement())
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        emptyConfigListReturnsNull()
        workspaceConfigReturnsValue(PATH1, workspaceConfigNoSaved)

        source.test {
            assertEquals(listOf(rootConfig, workspaceConfigNoSaved), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun transformFlow_whenWorkspaceConfigIsNull_returnsListWithJustRoot() = runTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsNull()

        source.test {
            assertEquals(listOf(rootConfig), awaitItem())
            awaitComplete()
        }
    }

    private fun emptyConfigListReturnsNull() =
        every {
            reader.firstConfigFile(emptyList())
        } returns null

    private fun rootConfigReturnsNull() =
        every {
            reader.firstConfigFile(ConfigurationPaths.RootConfig.searchPaths)
        } returns null

    private fun rootConfigReturnsValue() =
        every {
            reader.firstConfigFile(ConfigurationPaths.RootConfig.searchPaths)
        } returns rootConfig

    private fun savedConfigReturnsNull() =
        every {
            reader.firstConfigFile(ConfigurationPaths.savedConfigSearchPaths(workSpaceConfig)!!)
        } returns null

    private fun savedConfigReturnsValue() =
        every {
            reader.firstConfigFile(ConfigurationPaths.savedConfigSearchPaths(workSpaceConfig)!!)
        } returns savedConfig

    private fun workspaceConfigReturnsNull() =
        every {
            reader.firstConfigFile(ConfigurationPaths.workspaceSearchPaths(PATH1.toPath())!!)
        } returns null

    private fun workspaceConfigReturnsValue(path: String, value: RawTixConfiguration = workSpaceConfig) =
        every {
            reader.firstConfigFile(ConfigurationPaths.workspaceSearchPaths(path.toPath())!!)
        } returns value
}