package org.tix.platform

import kotlinx.browser.window
import org.w3c.dom.get

actual object PlatformEnv : Env {
    actual override operator fun get(name: String) = window[name] as? String ?: ""
}