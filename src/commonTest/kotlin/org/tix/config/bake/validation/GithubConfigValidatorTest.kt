package org.tix.config.bake.validation

import org.tix.config.data.raw.RawGithubConfiguration
import kotlin.test.Test
import kotlin.test.assertFailsWith

class GithubConfigValidatorTest {
    @Test
    fun validate_doesNotThrow_whenAllRequiredPropertiesExist() {
        val config = RawGithubConfiguration(owner = "owner", repo = "repo")
        GithubConfigValidator.validate(config)
    }

    @Test
    fun validate_throws_whenOwnerIsMissing() {
        val config = RawGithubConfiguration(repo = "repo")
        assertFailsWith(ConfigValidationException::class) { GithubConfigValidator.validate(config) }
    }

    @Test
    fun validate_throws_whenRepoIsMissing() {
        val config = RawGithubConfiguration(owner = "owner")
        assertFailsWith(ConfigValidationException::class) { GithubConfigValidator.validate(config) }
    }
}