package org.tix.config.reader

import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.config.serialize.ConfigurationSerializers
import org.tix.platform.io.FileIO
import kotlin.test.expect

class RawTixConfigurationReaderTest {
    private companion object {
        val JSON_PATH = "path/tix.json".toPath()
    }

    private val config = RawTixConfiguration(include = DynamicProperty(string = "included"))
    private val fileIO = mockk<FileIO<String>>()
    private val fileReader = ConfigurationFileReader(fileIO)
    private val jsonString = ConfigurationSerializers.json().encodeToString(config)
    private val reader = RawTixConfigurationReader(fileReader)

    @Test
    fun firstConfigFile() {
        every { fileIO.read(JSON_PATH.toString()) } returns jsonString
        expect(config) { reader.firstConfigFile(listOf(JSON_PATH)) }
    }
}