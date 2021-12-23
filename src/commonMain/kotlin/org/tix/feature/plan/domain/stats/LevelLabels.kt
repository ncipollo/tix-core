package org.tix.feature.plan.domain.stats

interface LevelLabels {
    fun singularLabel(level: Int): String
    fun pluralLabel(level: Int): String
    val levelCount: Int

    fun label(level: Int, count: Int) =
        if (count != 1) {
            pluralLabel(level)
        } else {
            singularLabel(level)
        }

    fun capitalizedLabel(level: Int, count: Int) =
        label(level, count)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}