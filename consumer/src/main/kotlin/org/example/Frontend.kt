package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class Frontend(private val backendUrl: String) {

    suspend fun getFruits(): String {

        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                })
            }
        }

        val response = client.get("$backendUrl/fruits") {
            contentType(ContentType.Application.Json)
        }

        return response.bodyAsText()
    }
}