package org.tix.integrations.github.rest.paging

fun String.parseNextLink(): String? =
    Regex("""(?<=<)(\S*)(?=>; rel="[Nn]ext")""")
        .find(this, 0)
        ?.groupValues
        ?.firstOrNull()