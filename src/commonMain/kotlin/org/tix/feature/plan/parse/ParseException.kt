package org.tix.feature.plan.parse

class ParseException(message: String) : RuntimeException(message)

fun parseError(message: Any): Nothing = throw ParseException(message.toString())