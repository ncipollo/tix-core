package org.tix.config.bake.validation

import org.tix.config.data.raw.RawJiraConfiguration
import kotlin.test.Test
import kotlin.test.assertFailsWith

class JiraConfigValidatorTest {
    @Test
    fun validate_doesNotThrow_whenAllRequiredPropertiesExist() {
        val config = RawJiraConfiguration(url = "url")
        JiraConfigValidator.validate(config)
    }

    @Test
    fun validate_throws_whenUrlIsMissing() {
        val config = RawJiraConfiguration()
        assertFailsWith(ConfigValidationException::class) { JiraConfigValidator.validate(config) }
    }
}