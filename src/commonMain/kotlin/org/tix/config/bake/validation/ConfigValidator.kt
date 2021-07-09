package org.tix.config.bake.validation

interface ConfigValidator<T> {
    val configName: String

    fun missingProperties(config: T): List<String>

    fun validate(config: T) {
        missingProperties(config)
            .takeUnless { it.isEmpty() }
            ?.let { throw ConfigValidationException(configName, it) }
    }
}