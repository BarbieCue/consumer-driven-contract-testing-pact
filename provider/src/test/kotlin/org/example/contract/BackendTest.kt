package org.example.contract

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Consumer
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.State
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.example.Backend
import org.example.Fruit
import org.example.fruits
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

@Consumer("Frontend")
@Provider("Backend")
@PactBroker(host = "localhost", port = "80", scheme = "http")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BackendTest {

    @BeforeAll
    fun startBackend() {

        // Have a real provider instance running
        Backend().start("localhost", 8080, false)
    }

    @State("there are fruits")
    fun `backend has some fruits`() {
        fruits = listOf(
            Fruit("apple", "germany", 200, 2.34),
            Fruit("orange", "spain", 240, 2.02)
        )
    }

    @State("there are no fruits")
    fun `backend does not have fruits`() {
        fruits = emptyList()
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun verifyFrontendPact(context: PactVerificationContext) {

        // Gets the pact from the broker and verifies the backend instance against it
        context.target = HttpTestTarget(host="localhost", port=8080, path="/fruits")
        context.verifyInteraction()
    }
}