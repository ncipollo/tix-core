package org.tix.feature.plan.domain.ticket

internal fun ticketNumber(key: String) =
    key.removePrefix("#")
        .toLongOrNull() ?: error("unable to convert key into a number")