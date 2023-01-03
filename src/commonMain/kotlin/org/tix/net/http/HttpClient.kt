package org.tix.net.http

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.tix.serialize.dynamic.DynamicElementJsonSerializer

fun httpClient(authMethod: AuthMethod = AuthMethod.None) = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            isLenient = false
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            useArrayPolymorphism = false
            serializersModule = SerializersModule {
                contextual(DynamicElementJsonSerializer)
            }
        })
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
            sendWithoutRequest { true }
        }
    }
}

private fun HttpClientConfig<*>.configureOAuth2TokenAuth(authMethod: AuthMethod.OAuth2Token) {
    defaultRequest {
        header("Authorization", "token ${authMethod.token}")
    }
}