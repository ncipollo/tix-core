package org.tix.config.domain

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath
import org.tix.config.ConfigurationPaths
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.domain.transform
import org.tix.test.runBlockingTest
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
        include = DynamicProperty(string = "saved"),
        variables = mapOf("workspace" to "workspace")
    )

    private val reader = mockk<RawTixConfigurationReader>()
    private val useCase = ConfigurationReadUseCase(reader)

    @Test
    fun transformFlow_whenAllConfigsAreNull_returnsEmptyList() = runBlockingTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsNull()
        workspaceConfigReturnsNull()

        source.test {
            assertEquals(emptyList(), expectItem())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_whenAllConfigsAreRead_returnsAllConfigs() = runBlockingTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        savedConfigReturnsValue()

        source.test {
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), expectItem())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_whenMultiplePathsAreProvided_returnsMultipleConfigLists() = runBlockingTest {
        val source = flowOf(PATH1, PATH2).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        workspaceConfigReturnsValue(PATH2)
        savedConfigReturnsValue()

        source.test {
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), expectItem())
            assertEquals(listOf(rootConfig, savedConfig, workSpaceConfig), expectItem())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_whenSavedConfigIsNull_returnsListWithoutSavedConfig() = runBlockingTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsValue(PATH1)
        savedConfigReturnsNull()

        source.test {
            assertEquals(listOf(rootConfig, workSpaceConfig), expectItem())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_whenWorkspaceConfigHasNoInclude_returnsListWithoutSavedConfig() = runBlockingTest {
        val workspaceConfigNoSaved = workSpaceConfig.copy(include = DynamicProperty())
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        emptyConfigListReturnsNull()
        workspaceConfigReturnsValue(PATH1, workspaceConfigNoSaved)

        source.test {
            assertEquals(listOf(rootConfig, workspaceConfigNoSaved), expectItem())
            expectComplete()
        }
    }

    @Test
    fun transformFlow_whenWorkspaceConfigIsNull_returnsListWithJustRoot() = runBlockingTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsValue()
        workspaceConfigReturnsNull()

        source.test {
            assertEquals(listOf(rootConfig), expectItem())
            expectComplete()
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