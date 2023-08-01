package org.tix.feature.plan.domain.parse

import okio.FileSystem
import okio.Path
import org.tix.platform.fs.defaultFileSystem
import org.tix.platform.path.pathByExpandingTilde

class MarkdownSourceValidator(private val fileSystem: FileSystem = defaultFileSystem) {
    fun validate(markdownSource: MarkdownSource) =
        when(markdownSource) {
            is MarkdownFileSource -> validateMarkdownFile(markdownSource)
            is MarkdownTextSource -> validateMarkdownText(markdownSource)
        }

    private fun validateMarkdownFile(fileSource: MarkdownFileSource) {
        val path = fileSource.path.pathByExpandingTilde()
        if (!markdownPathExists(path)) {
            throw MarkdownSourceException("markdown file doesn't exist at path $path")
        }
        if (isMarkdownPathADirectory(path)) {
            throw MarkdownSourceException("markdown path pointed to a directory")
        }
    }

    private fun isMarkdownPathADirectory(path: Path) = fileSystem.metadata(path).isDirectory

    private fun markdownPathExists(path: Path) = fileSystem.exists(path)

    private fun validateMarkdownText(textSource: MarkdownTextSource) {
        if (textSource.markdown.isBlank()) {
            throw MarkdownSourceException("markdown text was blank")
        }
    }
}

data class MarkdownSourceException(override val message: String): RuntimeException(message)