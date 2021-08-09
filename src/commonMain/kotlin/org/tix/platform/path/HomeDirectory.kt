package org.tix.platform.path

import okio.ExperimentalFileSystem
import okio.Path
import okio.Path.Companion.toPath

internal expect val homeDirectory: String

@ExperimentalFileSystem
internal fun String.pathByExpandingTilde(): Path = replaceFirst("~/", "$homeDirectory/").toPath()

object NoHomeDirectoryException: RuntimeException("no home directory found")

fun noHomeError(): Nothing = throw NoHomeDirectoryException