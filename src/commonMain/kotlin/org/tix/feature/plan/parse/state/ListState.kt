package org.tix.feature.plan.parse.state

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

    val currentLevel get() = listStack.size

    private fun startBulletList() = listStack.add(ListType.BULLET)

    private fun startOrderedList() = listStack.add(ListType.BULLET)

    private fun completeList() {
        listStack.removeLastOrNull()
    }
}