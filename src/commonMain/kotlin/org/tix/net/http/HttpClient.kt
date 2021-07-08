package org.tix.net.http

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.auth.providers.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

fun httpClient(authMethod: AuthMethod = AuthMethod.None) = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer()
    }
    configureAuth(authMethod)
}

private fun HttpClientConfig<*>.configureAuth(authMethod: AuthMethod) {
    when (authMethod) {
        is AuthMethod.Basic -> configureBasicAuth(authMethod)
        is AuthMethod.OAuth2Token -> configureOAuth2TokenAuth(authMethod)
        AuthMethod.None -> Unit
    }
}

private fun HttpClientConfig<*>.configureBasicAuth(authMethod: AuthMethod.Basic) {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(authMethod.username, authMethod.password)
            }
        }
    }
}

private fun HttpClientConfig<*>.configureOAuth2TokenAuth(authMethod: AuthMethod.OAuth2Token) {
    defaultRequest {
        header("Authorization", "token ${authMethod.token}")
    }
}