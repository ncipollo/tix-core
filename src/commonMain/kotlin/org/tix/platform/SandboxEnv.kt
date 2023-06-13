package org.tix.platform

/**
 * This environment wrapper may be used to help limit access to the environment when running tix on platforms where
 * untrusted interactions may be taking place (for example on a server which generates tickets from user input).
 */
class SandboxEnv(
    private val wrappedEnv: Env,
    private val acceptCheck: (String) -> (Boolean)
) : Env {
    override fun get(name: String): String {
        if (!acceptCheck(name)) {
            return ""
        }
        return wrappedEnv[name]
    }
}