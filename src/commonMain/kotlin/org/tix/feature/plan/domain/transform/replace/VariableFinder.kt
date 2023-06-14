package org.tix.feature.plan.domain.transform.replace

internal fun String.findVariables(metadata: TransformVariableMetadata): String? {
    if (!startsWith(metadata.variableToken) ||
        metadata.sortedNames.isEmpty() ||
        metadata.shortestNameLength > length) {
        return null
    }

    val tokenLength = metadata.variableToken.length
    var complete = false
    val names = metadata.sortedNames
    var index = metadata.shortestNameLength - 1
    var match: String? = null

    while (!complete) {
        val substring = substring(0..index)
        // Look to see if the current substring matches anything yet.
        match = names.firstOrNull { substring == it }

        index++
        complete = (match != null || // We have a match, we are done
                names.isEmpty() || // We ruled out everything, bail
                index >= length || // We are at the end of the string, bail
                index >= metadata.longestNameLength || // We are past the point where we could match any var
                (substring.length > tokenLength && substring.endsWith(metadata.variableToken))) // We've found another token
    }

    return match
}