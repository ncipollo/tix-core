package org.tix.feature.plan.domain.transform.replace

import org.tix.feature.plan.domain.transform.TransformVariableMap

/**
 * This function will find and replace variables within the source string. This should be more optimal than most can'd
 * mechanisms of replacing values in a string since it will only traverse the string one. It will also only look to
 * perform replacements if it finds a variable token ('$' by default).
 */
internal fun String.replaceVariables(variableMap: TransformVariableMap): String {
    if (isEmpty()) {
        return this
    }

    val metadata = variableMap.variableMetadata
    var replacedString = ""
    var currentIndex = 0
    while (currentIndex < length) {
        val nextTokenIndex = this.indexOf(string = metadata.variableToken, startIndex = currentIndex)
        if (nextTokenIndex == -1) {
            // No Variable token found, we can just add the remaining string to the result.
            replacedString += substring(currentIndex)
            currentIndex = length
        } else {
            // Create a substring up until the next token, then add that to the result.
            val beforeToken = substring(currentIndex, nextTokenIndex)
            replacedString += beforeToken
            currentIndex += beforeToken.length

            // Now look to see if we have a variable.
            val remainingString = substring(nextTokenIndex)
            val foundVariable = remainingString.findVariables(metadata)

            if (foundVariable != null) {
                // We found a variable, add its value to the result, then increment the index by the length of the
                // variable name. Note the variable name is of a different size than the value most likely, so we want
                // to increment the source index by the size of the variable name, not the value.
                replacedString += variableMap[foundVariable]
                currentIndex += foundVariable.length
            } else {
                // No variable found in the map, simple add the variable token then move the index past it.
                replacedString += metadata.variableToken
                currentIndex += metadata.variableToken.length
            }
        }
    }
    // use this.indexOf()
    return replacedString
}