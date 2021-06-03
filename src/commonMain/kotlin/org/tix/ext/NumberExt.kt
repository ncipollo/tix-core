package org.tix.ext

fun Number.isWholeNumber(precision: Double = 0.00001) = toDouble().rem(1) < precision