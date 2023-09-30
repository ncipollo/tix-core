package org.tix.config.bake

import org.tix.config.data.MatrixEntryConfiguration
import org.tix.serialize.dynamic.DynamicElement

object MatrixBaker {
    fun bake(matrix: Map<String, List<DynamicElement>>) =
        matrix.entries.associate { (key, value) ->
            key to value.map { it.toMatrixElement(key) }
        }


    private fun DynamicElement.toMatrixElement(key: String) =
        MatrixEntryConfiguration(asStringMap(key))
}