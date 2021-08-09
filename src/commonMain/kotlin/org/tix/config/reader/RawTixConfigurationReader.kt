package org.tix.config.reader

import okio.Path
import org.tix.config.data.raw.RawTixConfiguration

internal class RawTixConfigurationReader(private val configReader: ConfigurationFileReader) {
    fun firstConfigFile(paths: List<Path>) = configReader.firstConfigFile<RawTixConfiguration>(paths)
}