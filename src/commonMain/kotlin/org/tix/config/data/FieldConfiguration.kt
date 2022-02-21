package org.tix.config.data

interface FieldConfiguration {
    fun forLevel(level: Int):  Map<String, Any?>
}