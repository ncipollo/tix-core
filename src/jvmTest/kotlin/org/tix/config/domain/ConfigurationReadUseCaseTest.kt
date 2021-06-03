package org.tix.config.domain

import app.cash.turbine.test
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import okio.Path.Companion.toPath
import org.tix.config.ConfigurationPaths
import org.tix.config.data.TixConfiguration
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.domain.transform
import org.tix.test.runBlockingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationReadUseCaseTest {
    private companion object {
        const val PATH1 = "/path1/tix.md"
        const val PATH2 = "/path2/tix.md"
    }

    private val rootConfig = TixConfiguration(variables = mapOf("root" to "config"))
    private val savedConfig = TixConfiguration(variables = mapOf("saved" to "config"))
    private val workSpaceConfig = TixConfiguration(
        include = DynamicProperty(string = "saved"),
        variables = mapOf("workspace" to "workspace")
    )

    private val reader = mockk<ConfigurationReader>()
    private val useCase = ConfigurationReadUseCase(reader)

    @Test
    fun transformFlow_whenAllConfigsAreNull_returnsEmptyList() = runBlockingTest {
        val source = flowOf(PATH1).transform(useCase)

        rootConfigReturnsNull()
        workspaceConfigReturnsNull(PATH1)

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
        workspaceConfigReturnsNull(PATH1)

        source.test {
            assertEquals(listOf(rootConfig), expectItem())
            expectComplete()
        }
    }

    private fun rootConfigReturnsNull() =
        ConfigurationPaths.RootConfig.searchPaths.forEach {
            every { reader.readConfig(it) } returns null
        }

    private fun rootConfigReturnsValue() =
        ConfigurationPaths.RootConfig.searchPaths.forEach {
            every { reader.readConfig(it) } returns rootConfig
        }

    private fun savedConfigReturnsNull() =
        ConfigurationPaths.savedConfigSearchPaths(workSpaceConfig)!!.forEach {
            every { reader.readConfig(it) } returns null
        }

    private fun savedConfigReturnsValue() =
        ConfigurationPaths.savedConfigSearchPaths(workSpaceConfig)!!.forEach {
            every { reader.readConfig(it) } returns savedConfig
        }

    private fun workspaceConfigReturnsNull(path: String) =
        ConfigurationPaths.workspaceSearchPaths(path.toPath())!!.forEach {
            every { reader.readConfig(it) } returns null
        }

    private fun workspaceConfigReturnsValue(path: String, value: TixConfiguration = workSpaceConfig) =
        ConfigurationPaths.workspaceSearchPaths(path.toPath())!!.forEach {
            every { reader.readConfig(it) } returns value
        }
}