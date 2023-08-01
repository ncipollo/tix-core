package org.tix.config.domain.reader

import io.mockk.every
import io.mockk.mockk
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.expect

class SavedConfigurationReaderTest {
    private companion object {
        const val OPTIONS_SAVED_CONFIG_NAME = "options_saved_config"
        const val WORKSPACE_SAVED_CONFIG_NAME = "workspace_saved_config"
    }

    private val configPaths = ConfigurationPaths()
    private val optionsSavedConfig = RawTixConfiguration(variables = mapOf("saved" to "options_saved"))
    private val workspaceConfig = RawTixConfiguration(
        include = DynamicElement(WORKSPACE_SAVED_CONFIG_NAME),
        variables = mapOf("saved" to "workspace")
    )
    private val workspaceSavedConfig = RawTixConfiguration(variables = mapOf("saved" to "workspace_saved"))
    private val reader = mockk<RawTixConfigurationReader> {
        every { firstConfigFile(emptyList()) } returns null
        every {
            firstConfigFile(configPaths.workspaceIncludedConfigSearchPaths(workspaceConfig))
        } returns workspaceSavedConfig

        every {
            firstConfigFile(configPaths.savedConfigSearchPaths(OPTIONS_SAVED_CONFIG_NAME))
        } returns optionsSavedConfig
    }
    private val configReader = SavedConfigurationReader(configPaths, reader)

    @Test
    fun readSavedConfigs_noSavedConfigs() {
        val configOptions = ConfigurationSourceOptions()
        expect(emptyList()) {
            configReader.readSavedConfigs(configOptions, null)
        }
    }

    @Test
    fun readSavedConfigs_withBothConfigs() {
        val configOptions = ConfigurationSourceOptions(savedConfigName = OPTIONS_SAVED_CONFIG_NAME)
        expect(listOf(workspaceSavedConfig, optionsSavedConfig)) {
            configReader.readSavedConfigs(configOptions, workspaceConfig)
        }
    }

    @Test
    fun readSavedConfigs_withOptionsSavedConfig() {
        val configOptions = ConfigurationSourceOptions(savedConfigName = OPTIONS_SAVED_CONFIG_NAME)
        expect(listOf(optionsSavedConfig)) {
            configReader.readSavedConfigs(configOptions, null)
        }
    }

    @Test
    fun readSavedConfigs_withWorkspaceSavedConfig() {
        val configOptions = ConfigurationSourceOptions()
        expect(listOf(workspaceSavedConfig)) {
            configReader.readSavedConfigs(configOptions, workspaceConfig)
        }
    }
}