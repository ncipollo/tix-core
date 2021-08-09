package org.tix.integrations.jira

import org.tix.config.data.JiraConfiguration
import org.tix.integrations.jira.field.FieldApi
import org.tix.integrations.jira.issue.IssueApi
import org.tix.integrations.jira.transition.TransitionApi
import org.tix.integrations.shared.custom.CustomApi
import org.tix.net.BaseUrl
import org.tix.net.http.httpClient

class JiraApi(configuration: JiraConfiguration) {
    private val baseUrl = BaseUrl(configuration.url)
    private val client = httpClient(jiraAuthMethod(configuration))

    val custom = CustomApi(baseUrl, client)
    val field = FieldApi(baseUrl, client)
    val issue = IssueApi(baseUrl, client)
    val transition = TransitionApi(baseUrl, client)
}