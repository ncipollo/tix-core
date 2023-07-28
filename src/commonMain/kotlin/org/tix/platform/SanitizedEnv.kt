package org.tix.platform

import org.tix.config.reader.auth.AuthEnvVariables

/**
 * An environment which has been sanitized to exclude tix auth variables.
 */
fun sanitizedEnv(env: Env) = SandboxEnv(env) {
    !AuthEnvVariables.allVariables.contains(it)
}