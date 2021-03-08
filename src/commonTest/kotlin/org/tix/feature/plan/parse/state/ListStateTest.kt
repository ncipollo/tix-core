package org.tix.feature.plan.parse.state

import kotlin.test.Test
import kotlin.test.assertFails
import kotlin.test.expect

class ListStateTest {
    private val listState = ListState()

    @Test
    fun currentLevel() {
        expect(-1) { listState.currentLevel }
        listState.buildBulletList {
            expect(0) { listState.currentLevel }
            listState.buildOrderedList {
                expect(1) { listState.currentLevel }
            }
            expect(0) { listState.currentLevel }
        }
        expect(-1) { listState.currentLevel }
    }

    @Test
    fun currentType() {
        assertFails { listState.currentType }
        listState.buildBulletList {
            expect(ListType.BULLET) {listState.currentType}
            listState.buildOrderedList {
                expect(ListType.ORDERED) {listState.currentType}
            }
            expect(ListType.BULLET) {listState.currentType}
        }
        assertFails { listState.currentType }
    }
}