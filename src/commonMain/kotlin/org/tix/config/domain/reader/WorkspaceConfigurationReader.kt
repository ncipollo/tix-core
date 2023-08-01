package org.tix.config.domain.reader

import org.tix.config.ConfigurationPaths
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader
import org.tix.platform.path.pathByExpandingTilde

internal class WorkspaceConfigurationReader(
    private val configPaths: ConfigurationPaths,
    private val reader: RawTixConfigurationReader
) {
    fun readWorkspaceConfig(configOptions: ConfigurationSourceOptions) =
        configOptions.workspaceDirectory
            ?.let { configPaths.workspaceSearchPaths(it.pathByExpandingTilde()) }
            ?.let { reader.firstConfigFile(it) }
}