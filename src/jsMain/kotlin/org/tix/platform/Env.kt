package org.tix.platform

import kotlinx.browser.window
import org.w3c.dom.get

actual object Env {
    actual operator fun get(name: String) = window[name] as? String ?: ""
}