package org.tix.integrations.github.rest.paging

import kotlin.test.Test
import kotlin.test.expect

class LinkHeaderParserTest {
    @Test
    fun parseNextLink_emptyString() {
        expect(null) { "".parseNextLink() }
    }

    @Test
    fun parseNextLink_headerWithNextLink_lowercaseNext() {
        expect("https://api.github.com/repositories/1300192/issues?per_page=2&page=2") {
            "<https://api.github.com/repositories/1300192/issues?per_page=2&page=2>; rel=\"next\", <https://api.github.com/repositories/1300192/issues?per_page=2&page=7715>; rel=\"last\""
                .parseNextLink()
        }
    }

    @Test
    fun parseNextLink_headerWithNextLink_uppercaseNext() {
        expect("https://api.github.com/repositories/1300192/issues?per_page=2&page=2") {
            "<https://api.github.com/repositories/1300192/issues?per_page=2&page=2>; rel=\"Next\", <https://api.github.com/repositories/1300192/issues?per_page=2&page=7715>; rel=\"last\""
                .parseNextLink()
        }
    }

    @Test
    fun parseNextLink_headerWithNextLink_onlyNext() {
        expect("https://api.github.com/repositories/1300192/issues?per_page=2&page=2") {
            "<https://api.github.com/repositories/1300192/issues?per_page=2&page=2>; rel=\"next\""
                .parseNextLink()
        }
    }
}