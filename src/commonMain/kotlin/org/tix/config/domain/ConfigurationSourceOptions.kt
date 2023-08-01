package org.tix.config.domain

import org.tix.platform.path.pathByExpandingTilde

data class ConfigurationSourceOptions(
    val workspaceDirectory: String? = null,
    val savedConfigName: String? = null,
    val includeRootConfig: Boolean = true
) {
    companion object {
        fun forMarkdownSource(markdownPath: String, includeConfigName: String? = null) =
            ConfigurationSourceOptions(
                workspaceDirectory = workspaceFromMarkdownPath(markdownPath),
                savedConfigName = includeConfigName
            )

        private fun workspaceFromMarkdownPath(markdownPath: String) =
            markdownPath.pathByExpandingTilde().parent.toString()
    }
}