package org.tix.config.merge

import org.tix.config.data.raw.RawTixConfiguration

fun RawTixConfiguration.merge(overlay: RawTixConfiguration) = RawTixConfiguration(
    include = overlay.include,// We always want to take the top include
    github = github?.merge(overlay.github) ?: overlay.github,
    jira = jira?.merge(overlay.jira) ?: overlay.jira,
    variables = variables + overlay.variables,
    variableToken = overlay.variableToken ?: variableToken
)

fun List<RawTixConfiguration>.flatten() = reduce { acc, configuration -> acc.merge(configuration) }