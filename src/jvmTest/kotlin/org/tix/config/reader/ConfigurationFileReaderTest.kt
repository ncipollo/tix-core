package org.tix.config.reader

import io.mockk.every
import io.mockk.mockk
import kotlinx.serialization.encodeToString
import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.io.FileIO
import org.tix.serialize.TixSerializers
import org.tix.serialize.dynamic.DynamicElement
import java.io.IOException
import kotlin.test.assertFails
import kotlin.test.expect

class ConfigurationFileReaderTest {
    private companion object {
        val JSON_PATH = "path/tix.json".toPath()
        val YML_PATH = "path/tix.yml".toPath()
    }

    private val config = RawTixConfiguration(include = DynamicElement("included"))
    private val fileIO = mockk<FileIO<String>>()
    private val jsonString = TixSerializers.json().encodeToString(config)
    private val reader = ConfigurationFileReader(fileIO)
    private val ymlString = TixSerializers.yaml().encodeToString(config)

    @Test
    fun firstConfigFile_whenAllConfigsAreNull_returnsNull() {
        every { fileIO.read(JSON_PATH.toString()) } throws IOException()

        expect(null) { reader.firstConfigFile<RawTixConfiguration>(listOf(JSON_PATH)) }
    }

    @Test
    fun firstConfigFile_whenAtLeastOneConfigIsNotNull_returnsFirstNotNullConfig() {
        every { fileIO.read(JSON_PATH.toString()) } throws IOException()
        every { fileIO.read(YML_PATH.toString()) } returns ymlString

        expect(config) { reader.firstConfigFile<RawTixConfiguration>(listOf(JSON_PATH, YML_PATH)) }
    }

    @Test
    fun firstConfigFile_whenEmptyListIsProvided_returnsFirstNotNullConfig() {
        expect(null) { reader.firstConfigFile<RawTixConfiguration>(emptyList()) }
    }

    @Test
    fun readConfig_whenJsonPath_andParseFails_returnsNull() {
        every { fileIO.read(JSON_PATH.toString()) } returns ","
        assertFails {
            reader.readConfig<RawTixConfiguration>(JSON_PATH)
        }
    }

    @Test
    fun readConfig_whenJsonPath_andReadFails_returnsNull() {
        every { fileIO.read(JSON_PATH.toString()) } throws IOException()
        expect(null) { reader.readConfig<RawTixConfiguration>(JSON_PATH) }
    }

    @Test
    fun readConfig_whenJsonPath_andSucceeds_returnsConfig() {
        every { fileIO.read(JSON_PATH.toString()) } returns jsonString
        expect(config) { reader.readConfig<RawTixConfiguration>(JSON_PATH) }
    }

    @Test
    fun readConfig_whenYamlPath_andParseFails_returnsNull() {
        every { fileIO.read(YML_PATH.toString()) } returns ","
        assertFails {
            reader.readConfig<RawTixConfiguration>(YML_PATH)
        }
    }

    @Test
    fun readConfig_whenYamlPath_andReadFails_returnsNull() {
        every { fileIO.read(YML_PATH.toString()) } throws IOException()
        expect(null) { reader.readConfig<RawTixConfiguration>(YML_PATH) }
    }

    @Test
    fun readConfig_whenYamlPath_andSucceeds_returnsConfig() {
        every { fileIO.read(YML_PATH.toString()) } returns ymlString
        expect(config) { reader.readConfig<RawTixConfiguration>(YML_PATH) }
    }
}