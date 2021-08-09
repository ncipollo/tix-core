package org.tix.config

import okio.Path
import org.tix.config.data.raw.RawTixConfiguration
import org.tix.platform.path.pathByExpandingTilde

internal object ConfigurationPaths {
    private val extensions = listOf("yml", "json")
    private val savedConfigs = "~/.tix/configs".pathByExpandingTilde()

    object RootConfig {
        val searchPaths = extensions.map { "~/.tix/config.$it".pathByExpandingTilde() }
    }

    fun savedConfigSearchPaths(config: RawTixConfiguration?) =
        config?.include
            ?.asList()
            ?.filterIsInstance<String>()
            ?.flatMap { name ->
                extensions.map { savedConfigs / "$name.$it" }
            }

    fun workspaceSearchPaths(markdownPath: Path) =
        markdownPath.parent?.let { dir ->
            extensions.map { dir / "tix.$it" }
        }
}