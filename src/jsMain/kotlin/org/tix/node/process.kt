package org.tix.node

external interface Process {
    var env: dynamic
}

@JsModule("process")
@JsNonModule
external val process: Process

