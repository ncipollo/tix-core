package org.tix.config.domain.reader

import io.mockk.every
import io.mockk.mockk
import okio.Path.Companion.toPath
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader
import kotlin.test.Test
import kotlin.test.expect

class WorkspaceConfigurationReaderTest {
    private companion object {
        const val DIR = "/path1/"
        const val PATH = "/path1/tix.md"
    }
    private val configPaths = ConfigurationPaths()
    private val workspaceConfig = RawTixConfiguration(variables = mapOf("workspace" to "workspace"))
    private val reader = mockk<RawTixConfigurationReader> {
        every { firstConfigFile(configPaths.workspaceSearchPaths(DIR.toPath())) } returns workspaceConfig
    }
    private val configReader = WorkspaceConfigurationReader(configPaths, reader)

    @Test
    fun readWorkspaceConfig_noWorkspace() {
        val configOptions = ConfigurationSourceOptions()
        expect(null) {
            configReader.readWorkspaceConfig(configOptions)
        }
    }

    @Test
    fun readWorkspaceConfig_withWorkspace() {
        val configOptions = ConfigurationSourceOptions(workspaceDirectory = DIR)
        expect(workspaceConfig) {
            configReader.readWorkspaceConfig(configOptions)
        }
    }
}