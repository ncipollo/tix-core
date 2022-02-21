package org.tix.feature.plan.domain.ticket.jira

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import org.tix.feature.plan.domain.ticket.PlanningContext
import org.tix.integrations.jira.field.Field
import org.tix.integrations.jira.issue.Component
import org.tix.integrations.jira.issue.IssueFields
import org.tix.integrations.jira.issue.IssueType
import org.tix.integrations.jira.issue.Parent
import org.tix.integrations.jira.project.Project
import org.tix.integrations.jira.version.Version
import kotlin.test.Test
import kotlin.test.expect

class JiraIssueFieldsBuilderTest {
    private val description = "description"
    private val summary = "summary"
    private val cache = JiraFieldCache(listOf(Field(id = "1", name = "unknown")))
    private val unknownsBuilder = JiraUnknownsBuilder(cache)

    @Test
    fun issueFields_parent_explicit_valid_returnsIssuesWithParentKey() {
        val context = PlanningContext<JiraPlanResult>()

        val fields = mapOf("parent" to "parent_key")
        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, fields, unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            parent = Parent(key = "parent_key"),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_parent_explicit_invalid_returnsIssuesWithNoParentKey() {
        val context = PlanningContext<JiraPlanResult>()

        val fields = mapOf("parent" to 1)
        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, fields, unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            parent = null,
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_parent_implicit_valid_returnsIssuesWithParentKey() {
        val context = PlanningContext(parentTicket = JiraPlanResult(id = "parent_id"))

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, emptyMap(), unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            parent = Parent(id = "parent_id"),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_parent_implicit_invalid_returnsIssuesWithParentKey() {
        val context = PlanningContext(parentTicket = JiraPlanResult())

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, emptyMap(), unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            parent = null,
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_parent_explicitAndImplicit_invalid_returnsIssuesWithNoParentKey() {
        val context = PlanningContext(parentTicket = JiraPlanResult(id = "parent_id"))

        val fields = mapOf("parent" to "parent_key")
        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, fields, unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            parent = Parent(key = "parent_key"),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_whenFieldsAreEmpty_level0_returnsEpicDefaultFields() {
        val context = PlanningContext<JiraPlanResult>()

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, emptyMap(), unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_whenFieldsAreEmpty_level1_returnsStoryDefaultFields() {
        val context = PlanningContext<JiraPlanResult>(level = 1)

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, emptyMap(), unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Story")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_whenFieldsAreEmpty_level2_returnsTaskDefaultFields() {
        val context = PlanningContext<JiraPlanResult>(level = 2)

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, emptyMap(), unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = emptyList(),
            labels = emptyList(),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Subtask")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_whenFieldsAreInvalid_returnFilteredAndDefaultIssueFields() {
        val context = PlanningContext<JiraPlanResult>()
        val fields = mapOf(
            "affects_versions" to "bad_list",
            "components" to 42,
            "fix_versions" to listOf(1, "2.1", 3),
            "labels" to 1.0f,
            "parent" to 42f,
            "project" to 42,
            "type" to listOf("foo")
        )

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, fields, unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = emptyList(),
            components = emptyList(),
            description = "description",
            fixVersions = listOf(Version(name = "2.1")),
            labels = emptyList(),
            project = Project(key = ""),
            summary = "summary",
            type = IssueType(name = "Epic")
        )
        expect(expected) { issueBuilder.issueFields() }
    }

    @Test
    fun issueFields_whenFieldsAreValid_returnValidIssueFields() {
        val context = PlanningContext<JiraPlanResult>()
        val fields = mapOf(
            "affects_versions" to listOf("1.0", "2.0", "3.0"),
            "components" to listOf("component1", "component2"),
            "fix_versions" to listOf("1.1", "2.1", "3.1"),
            "labels" to listOf("label1", "label2"),
            "project" to "tix",
            "type" to "story",
            "unknown" to "foo"
        )

        val issueBuilder = JiraIssueFieldsBuilder(context, summary, description, fields, unknownsBuilder)

        val expected = IssueFields(
            affectsVersions = listOf(Version(name = "1.0"), Version(name = "2.0"), Version(name = "3.0")),
            components = listOf(Component(name = "component1"), Component(name = "component2")),
            description = "description",
            fixVersions = listOf(Version(name = "1.1"), Version(name = "2.1"), Version(name = "3.1")),
            labels = listOf("label1", "label2"),
            project = Project(key = "tix"),
            summary = "summary",
            type = IssueType(name = "story"),
            unknowns = JsonObject(mapOf("1" to JsonPrimitive("foo")))
        )
        expect(expected) { issueBuilder.issueFields() }
    }
}