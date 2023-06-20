package org.tix.net.http

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.tix.serialize.dynamic.DynamicElementJsonSerializer

fun httpClient(engine: HttpClientEngine? = null, block: HttpClientConfig<*>.() -> Unit = {}) =
    if (engine != null) {
        HttpClient(engine) {
            expectSuccess = true
            apply(block)
        }
    } else {
        HttpClient {
            expectSuccess = true
            apply(block)
        }
    }

internal fun HttpClientConfig<*>.installContentNegotiation(useSnakeCase: Boolean = false) {
    install(ContentNegotiation) {
        json(httpJson(useSnakeCase))
    }
}

@OptIn(ExperimentalSerializationApi::class)
fun httpJson(useSnakeCase: Boolean = false) = Json {
    isLenient = false
    ignoreUnknownKeys = true
    allowSpecialFloatingPointValues = true
    serializersModule = SerializersModule {
        contextual(DynamicElementJsonSerializer)
    }
    if (useSnakeCase) {
        namingStrategy = JsonNamingStrategy.SnakeCase
    }
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