package org.tix.config.domain.reader

import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader

internal class SavedConfigurationReader(
    private val configPaths: ConfigurationPaths,
    private val reader: RawTixConfigurationReader
) {
    fun readSavedConfigs(
        configOptions: ConfigurationSourceOptions,
        markdownConfig: RawTixConfiguration?,
        workspaceConfig: RawTixConfiguration?
    ) = listOfNotNull(
        includedFromWorkspaceConfig(markdownConfig),
        includedFromWorkspaceConfig(workspaceConfig),
        includedFromConfigOptions(configOptions),
    )

    private fun includedFromWorkspaceConfig(workspaceConfig: RawTixConfiguration?) =
        configPaths.workspaceIncludedConfigSearchPaths(workspaceConfig).let {
            reader.firstConfigFile(it)
        }

    private fun includedFromConfigOptions(configOptions: ConfigurationSourceOptions) =
        configOptions.savedConfigName?.let {
            reader.firstConfigFile(configPaths.savedConfigSearchPaths(it))
        }
}