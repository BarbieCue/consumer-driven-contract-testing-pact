package org.example

import au.com.dius.pact.provider.junit5.HttpTestTarget
import au.com.dius.pact.provider.junit5.PactVerificationContext
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider
import au.com.dius.pact.provider.junitsupport.Consumer
import au.com.dius.pact.provider.junitsupport.Provider
import au.com.dius.pact.provider.junitsupport.loader.PactBroker
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.junit.jupiter.api.TestTemplate
import org.junit.jupiter.api.extension.ExtendWith

@Consumer("Frontend")
@Provider("Backend")
@PactBroker(host = "localhost", port = "80", scheme = "http")
class BackendTest {

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider::class)
    fun verifyFrontendPact(context: PactVerificationContext) {

        // Have a real provider instance running.
        Backend().start("localhost", 8080, false)

        // And verify the pact against it.
        context.target = HttpTestTarget(host="localhost", port=8080, path="/fruits")
        context.verifyInteraction()
    }
}