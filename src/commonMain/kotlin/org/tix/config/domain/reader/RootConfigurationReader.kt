package org.tix.config.domain.reader

import org.tix.config.ConfigurationPaths
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader

internal class RootConfigurationReader(
    private val configPaths: ConfigurationPaths,
    private val reader: RawTixConfigurationReader
) {
    fun readRootConfig(configurationSourceOptions: ConfigurationSourceOptions) =
        if (configurationSourceOptions.includeRootConfig) {
            reader.firstConfigFile(configPaths.rootConfigSearchPaths)
        } else {
            null
        }
}