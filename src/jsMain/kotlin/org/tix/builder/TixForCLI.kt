package org.tix.builder

import org.tix.Tix

actual fun tixForCLI(): Tix = error("Tix CLI is not supported in the browser")