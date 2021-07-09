package org.tix.config.reader.auth

import okio.Path.Companion.toPath
import org.junit.Test
import org.tix.config.data.auth.AuthSource
import org.tix.config.data.raw.RawAuthConfiguration
import org.tix.platform.path.pathByExpandingTilde
import kotlin.test.expect

class AuthConfigurationPathsTest {
    @Test
    fun searchPaths_whenAuthSourceIsLocalAndFileIsOmitted_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.LOCAL_FILE,)
        val expectedPaths = listOf("/path/tix_auth.yml", "/path/tix_auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }

    @Test
    fun searchPaths_whenAuthSourceIsLocalAndFileIsProvided_withExtension_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.LOCAL_FILE, file = "auth.json")
        val expectedPaths = listOf("/path/auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }

    @Test
    fun searchPaths_whenAuthSourceIsLocalAndFileIsProvided_withoutExtension_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.LOCAL_FILE, file = "auth")
        val expectedPaths = listOf("/path/auth.yml", "/path/auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }

    @Test
    fun searchPaths_whenAuthSourceIsTixAndFileIsOmitted_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.TIX_FILE,)
        val expectedPaths = listOf("~/.tix/auth/tix_auth.yml", "~/.tix/auth/tix_auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }

    @Test
    fun searchPaths_whenAuthSourceIsTixAndFileIsProvided_withExtension_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.TIX_FILE, file = "auth.json")
        val expectedPaths = listOf("~/.tix/auth/auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }

    @Test
    fun searchPaths_whenAuthSourceIsTixAndFileIsProvided_withoutExtension_returnsLocalSearchPaths() {
        val auth = RawAuthConfiguration(source = AuthSource.TIX_FILE, file = "auth")
        val expectedPaths = listOf("~/.tix/auth/auth.yml", "~/.tix/auth/auth.json")
            .map { it.pathByExpandingTilde() }

        expect(expectedPaths) { AuthConfigurationPaths.searchPaths("/path/tix.md".toPath(), auth) }
    }
}