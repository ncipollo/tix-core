package org.tix.integrations.github.graphql.error

import kotlin.test.Test
import kotlin.test.expect

class GithubQueryErrorExceptionTest {
    @Test
    fun toString_message() {
        val error1 = QueryError(
            message = "message1",
            locations = listOf(
                QueryErrorLocation(0, 0),
                QueryErrorLocation(5, 10),
            )
        )
        val error2 = QueryError(
            message = "message2",
            locations = listOf(
                QueryErrorLocation(10, 0),
                QueryErrorLocation(15, 10),
            )
        )
        val response = GithubQueryErrorResponse(listOf(error1, error2))

        val expected = """
            Github Query Errors:
            - [0:0] message1
            - [5:10] message1
            - [10:0] message2
            - [15:10] message2
        """.trimIndent()
        expect(expected) {
            GithubQueryException(response).message
        }
    }
}