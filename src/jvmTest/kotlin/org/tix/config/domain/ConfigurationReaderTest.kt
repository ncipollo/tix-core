package org.tix.config.domain

import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.TixConfiguration
import org.tix.config.data.dynamic.DynamicProperty
import org.tix.config.serialize.ConfigurationSerializers
import org.tix.platform.io.FileIO
import java.io.IOException
import kotlin.test.expect

class ConfigurationReaderTest {
    private companion object {
        val JSON_PATH = "path/tix.json".toPath()
        val YML_PATH = "path/tix.yml".toPath()
    }

    private val config = TixConfiguration(include = DynamicProperty(string = "included"))
    private val fileIO = mockk<FileIO<String>>()
    private val jsonString = ConfigurationSerializers.json().encodeToString(config)
    private val reader = ConfigurationReader(fileIO)
    private val ymlString = ConfigurationSerializers.yaml().encodeToString(config)

    @Test
    fun readConfig_whenJsonPath_andParseFails_returnsNull() {
        every { fileIO.read(JSON_PATH.toString()) } returns ","
        expect(null) { reader.readConfig(JSON_PATH) }
    }

    @Test
    fun readConfig_whenJsonPath_andReadFails_returnsNull() {
        every { fileIO.read(JSON_PATH.toString()) } throws IOException()
        expect(null) { reader.readConfig(JSON_PATH) }
    }

    @Test
    fun readConfig_whenJsonPath_andSucceeds_returnsConfig() {
        every { fileIO.read(JSON_PATH.toString()) } returns jsonString
        expect(config) { reader.readConfig(JSON_PATH) }
    }

    @Test
    fun readConfig_whenYamlPath_andParseFails_returnsNull() {
        every { fileIO.read(YML_PATH.toString()) } returns ","
        expect(null) { reader.readConfig(YML_PATH) }
    }

    @Test
    fun readConfig_whenYamlPath_andReadFails_returnsNull() {
        every { fileIO.read(YML_PATH.toString()) } throws IOException()
        expect(null) { reader.readConfig(YML_PATH) }
    }

    @Test
    fun readConfig_whenYamlPath_andSucceeds_returnsConfig() {
        every { fileIO.read(YML_PATH.toString()) } returns ymlString
        expect(config) { reader.readConfig(YML_PATH) }
    }
}