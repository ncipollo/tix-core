package org.tix.config.domain.reader

import io.mockk.every
import io.mockk.mockk
import org.tix.config.ConfigurationPaths
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.domain.ConfigurationSourceOptions
import org.tix.config.reader.RawTixConfigurationReader
import kotlin.test.Test
import kotlin.test.expect

class RootConfigurationReaderTest {
    private val configPaths = ConfigurationPaths()
    private val rootConfig = RawTixConfiguration(variables = mapOf("root" to "config"))
    private val reader = mockk<RawTixConfigurationReader> {
        every {  firstConfigFile(configPaths.rootConfigSearchPaths) } returns rootConfig
    }

    private val configReader = RootConfigurationReader(configPaths, reader)

    @Test
    fun readRootConfig_excludesRootConfig() {
        val configOptions = ConfigurationSourceOptions(includeRootConfig = false)
        expect(null) {
            configReader.readRootConfig(configOptions)
        }
    }

    @Test
    fun readRootConfig_includesRootConfig() {
        val configOptions = ConfigurationSourceOptions()
        expect(rootConfig) {
            configReader.readRootConfig(configOptions)
        }
    }
}