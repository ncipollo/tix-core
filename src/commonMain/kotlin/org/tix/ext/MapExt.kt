package org.tix.ext

inline fun <reified T, R> Map<String, Any?>.transform(key: String, default: T, transformer: (T) -> R): R {
    val value = this[key] as? T ?: default
    return transformer(value)
}

inline fun <reified T, R> Map<String, Any?>.transformList(key: String, default: T, transformer: (T) -> R): List<R> {
    val anyList = this[key] as? List<Any?> ?: emptyList()
    return anyList.map {
        val value = it as? T ?: default
        transformer(value)
    }
}

inline fun <reified T, R> Map<String, Any?>.transformFilteredList(
    key: String,
    transformer: (T) -> R
): List<R> {
    val anyList = this[key] as? List<Any?> ?: emptyList()
    return anyList
        .filterIsInstance<T>()
        .map { transformer(it) }
}