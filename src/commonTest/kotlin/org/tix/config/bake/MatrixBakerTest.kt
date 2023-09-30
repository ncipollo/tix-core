package org.tix.config.bake

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.serialize.dynamic.DynamicElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.expect

class MatrixBakerTest {
    @Test
    fun bake_emptyMatrix() {
        expect(emptyMap()) { MatrixBaker.bake(emptyMap()) }
    }

    @Test
    fun bake_fullMatrix() {
        val matrix = mapOf(
            "matrix_1" to listOf(DynamicElement("value_1"), DynamicElement("value_2")),
            "matrix_2" to listOf(DynamicElement(mapOf("key_3" to "value_3", "key_4" to "value_4"))),
        )

        val bakedMatrix = MatrixBaker.bake(matrix)
        val expected = mapOf(
            "matrix_1" to listOf(
                MatrixEntryConfiguration(mapOf("matrix_1" to "value_1")),
                MatrixEntryConfiguration(mapOf("matrix_1" to "value_2")),
            ),
            "matrix_2" to listOf(
                MatrixEntryConfiguration(
                    mapOf(
                        "key_3" to "value_3",
                        "key_4" to "value_4"
                    )
                )
            )
        )
        assertEquals(expected, bakedMatrix)
    }
}