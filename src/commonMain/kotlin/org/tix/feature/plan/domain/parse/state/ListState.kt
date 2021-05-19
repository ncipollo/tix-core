package org.tix.feature.plan.domain.parse.state

internal class ListState {
    private val listStack = ArrayList<ListType>()

    fun buildBulletList(buildBlock: () -> Unit) {
        startBulletList()
        buildBlock()
        completeList()
    }

    fun buildOrderedList(buildBlock: () -> Unit) {
        startOrderedList()
        buildBlock()
        completeList()
    }

    val currentType get() = listStack.lastOrNull() ?: error("no list started")

    val currentLevel get() = listStack.size - 1

    private fun startBulletList() = listStack.add(ListType.BULLET)

    private fun startOrderedList() = listStack.add(ListType.ORDERED)

    private fun completeList() {
        listStack.removeLastOrNull()
    }
}