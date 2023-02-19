package org.example

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

class Backend {

    fun start(host: String, port: Int, wait: Boolean) {
        embeddedServer(Netty, port = port, host = host, module = Application::module).start(wait = wait)
    }
}

// The backend state.
// For the sake of simplicity, we use a variable here instead of a database or similar.
// The value of fruits is changed accordingly by the "States" in the provider test
var fruits = emptyList<Fruit>()

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/fruits") {
            call.respond(fruits)
        }
    }
}

@Serializable
data class Fruit(
    val name: String,
    val originCountry: String,
    val amount: Int,
    val pricePerKilo: Double
)