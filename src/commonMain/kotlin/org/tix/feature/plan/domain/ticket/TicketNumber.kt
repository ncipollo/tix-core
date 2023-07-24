package org.tix.feature.plan.domain.ticket

internal fun ticketNumber(key: Any?) =
    key?.toString()
        ?.removePrefix("#")
        ?.toLongOrNull() ?: error("unable to convert key into a number")