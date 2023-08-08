package org.tix.integrations.jira

import org.tix.config.data.JiraConfiguration

class JiraApiFactory {
    fun api(configuration: JiraConfiguration) = JiraApi(configuration)
}