package org.tix.config.reader.auth

import okio.Path
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.platform.path.pathByExpandingTilde

internal object AuthConfigurationPaths {
    private val extensions = listOf("yml", "json")
    private val savedConfigs = "~/.tix/auth".pathByExpandingTilde()

    fun searchPaths(markdownPath: Path, config: RawAuthConfiguration) =
        when (source(config)) {
            AuthSource.ENV -> error("AuthSource.ENV should not be using a file reader")
            AuthSource.LOCAL_FILE -> localSearchPaths(markdownPath, config)
            AuthSource.TIX_FILE -> tixSearchPaths(config)
        }

    private fun source(config: RawAuthConfiguration) = config.source ?: AuthSource.LOCAL_FILE

    private fun localSearchPaths(markdownPath: Path, config: RawAuthConfiguration): List<Path> =
        markdownPath.parent?.let { dir ->
            val fileName = fileName(config)
            if (fileNameHasExtension(fileName)) {
                listOf(dir / fileName)
            } else {
                extensions.map { dir / "${fileName(config)}.$it" }
            }
        } ?: emptyList()

    private fun fileName(config: RawAuthConfiguration) = config.file ?: "tix_auth"

    private fun fileNameHasExtension(fileName: String) = fileName.contains('.')

    private fun tixSearchPaths(config: RawAuthConfiguration): List<Path> {
        val fileName = fileName(config)
        return if (fileNameHasExtension(fileName)) {
            listOf(savedConfigs / fileName)
        } else {
            extensions.map { savedConfigs / "${fileName(config)}.$it" }
        }
    }
}