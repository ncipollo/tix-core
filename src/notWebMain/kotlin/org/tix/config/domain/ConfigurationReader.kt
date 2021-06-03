package org.tix.config.domain

import kotlinx.serialization.decodeFromString
import okio.Path
import org.tix.config.data.TixConfiguration
import org.tix.config.serialize.ConfigurationSerializers
import org.tix.platform.io.FileIO

internal class ConfigurationReader(private val fileIO: FileIO<String>) {
    private val json = ConfigurationSerializers.json()
    private val yaml = ConfigurationSerializers.yaml()

    fun readConfig(path: Path): TixConfiguration? =
        if (path.name.endsWith(".json")) {
            runCatching { readJsonConfig(path) }.getOrNull()
        } else {
            runCatching { readYamlConfig(path) }.getOrNull()
        }

    private fun readJsonConfig(path: Path): TixConfiguration = json.decodeFromString(read(path))

    private fun readYamlConfig(path: Path): TixConfiguration = yaml.decodeFromString(read(path))

    private fun read(path: Path) = fileIO.read(path.toString())
}