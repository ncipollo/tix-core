package org.tix.config.bake.validation

data class ConfigValidationException(val configName: String, val missingProperties: List<String>) :
    RuntimeException(message(configName, missingProperties))

private fun message(configName: String, missingProperties: List<String>) =
    "$configName was missing properties: ${missingProperties.joinToString(", ")}"