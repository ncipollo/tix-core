package org.tix.config.reader

import kotlinx.serialization.decodeFromString
import okio.Path
import org.tix.config.serialize.ConfigurationSerializers
import org.tix.platform.io.FileIO

internal class ConfigurationFileReader(private val fileIO: FileIO<String>) {
    val json = ConfigurationSerializers.json()
    val yaml = ConfigurationSerializers.yaml()

    inline fun <reified T> firstConfigFile(paths: List<Path>?) =
        paths?.firstNotNullOfOrNull { readConfig<T>(it) }

    inline fun <reified T> readConfig(path: Path): T? =
        if (path.name.endsWith(".json")) {
            runCatching { readJsonConfig<T>(path) }.getOrNull()
        } else {
            runCatching { readYamlConfig<T>(path) }.getOrNull()
        }

    inline fun <reified T> readJsonConfig(path: Path): T = json.decodeFromString(read(path))

    inline fun <reified T> readYamlConfig(path: Path): T = yaml.decodeFromString(read(path))

    fun read(path: Path) = fileIO.read(path.toString())
}