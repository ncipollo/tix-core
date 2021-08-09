package org.tix.platform.path

import okio.ExperimentalFileSystem
import okio.Path.Companion.toPath
import org.tix.platform.fs.defaultFileSystem
import kotlin.test.Test
import kotlin.test.expect

@ExperimentalFileSystem
class HomeDirectoryTest {
    @Test
    fun pathByExpandingTilde_expandsTilde() {
        expect("$homeDirectory/test".toPath()) { "~/test".pathByExpandingTilde() }
    }

    @Test
    fun pathByExpandingTilde_homeIsNotEmpty() {
        expect(true) {
            defaultFileSystem.metadata("~/".pathByExpandingTilde()).isDirectory
        }
    }
}