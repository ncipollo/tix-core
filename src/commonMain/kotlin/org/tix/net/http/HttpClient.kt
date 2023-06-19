package org.tix.net.http

import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.tix.serialize.dynamic.DynamicElementJsonSerializer

fun httpClient(block: HttpClientConfig<*>.() -> Unit = {}) = HttpClient {
    expectSuccess = true
    install(ContentNegotiation) {
        json(Json {
            isLenient = false
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            serializersModule = SerializersModule {
                contextual(DynamicElementJsonSerializer)
            }
        })
    }
    apply(block)
}

internal fun HttpClientConfig<*>.configureBasicAuth(username: String, password: String) {
    install(Auth) {
        basic {
            credentials {
                BasicAuthCredentials(username, password)
            }
            sendWithoutRequest { true }
        }
    }
}