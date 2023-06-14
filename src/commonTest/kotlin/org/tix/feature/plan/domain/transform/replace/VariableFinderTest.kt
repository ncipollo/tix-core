package org.tix.feature.plan.domain.transform.replace

import kotlin.test.Test
import kotlin.test.expect

class VariableFinderTest {
    private val standardMetadata = TransformVariableMetadata(
        sortedNames = listOf("\$var1", "\$var2", "\$var1var2", "\$filtered1", "\$filtered2"),
        longestNameLength = "\$var1var2".length,
        shortestNameLength = "\$var1".length,
        variableToken = "$"
    )

    @Test
    fun findVariables_foundMatch_simpleVariable() {
        expect("\$var1") { "\$var1xxx".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_foundMatch_simpleVariable_stringIsSameLengthAsVar() {
        expect("\$var1") { "\$var1".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_foundMatch_variableContainingOtherVariables() {
        // We will just grab the first variable we see in this case (var1)
        expect("\$var1") { "\$var1var2xxx".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_emptyMetadata() {
        val metadata = TransformVariableMetadata(sortedNames = emptyList(),
            longestNameLength = 0,
            shortestNameLength = 0,
            variableToken = "$")
        expect(null) { "\$var1".findVariables(metadata) }
    }

    @Test
    fun findVariables_noMatch_filteredOutNames() {
        expect(null) { "\$foo1".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_noTokenAtStart() {
        expect(null) { " \$var1".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_reachEndOfSourceString() {
        expect(null) { "\$var45".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_reachLongestNameLength() {
        expect(null) { "\$var3var4var5".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_reachesSecondToken() {
        expect(null) { "\$var3\$var1".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_reachesSecondToken_immediately() {
        expect(null) { "\$\$var1".findVariables(standardMetadata) }
    }

    @Test
    fun findVariables_noMatch_shorterThenSmallestVariable() {
        expect(null) { "\$va".findVariables(standardMetadata) }
    }
}