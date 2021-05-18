package org.tix.platform.path

import okio.ExperimentalFileSystem
import okio.FileSystem
import okio.Path.Companion.toPath
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
            FileSystem.SYSTEM.metadata("~/".pathByExpandingTilde()).isDirectory
        }
    }
}