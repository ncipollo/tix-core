package org.tix.config.merge

import org.tix.config.data.TixConfiguration

fun TixConfiguration.merge(overlay: TixConfiguration) = TixConfiguration(
    include = overlay.include,// We always want to take the top include
    github = github.merge(overlay.github),
    jira = jira.merge(overlay.jira),
    variables = variables + overlay.variables
)

fun List<TixConfiguration>.flatten() = reduce { acc, configuration -> acc.merge(configuration) }