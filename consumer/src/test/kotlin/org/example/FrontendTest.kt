package org.example

import au.com.dius.pact.consumer.*
import au.com.dius.pact.consumer.dsl.PactDslJsonArray
import au.com.dius.pact.consumer.model.MockProviderConfig
import io.kotest.assertions.json.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class FrontendTest {

    @Test
    fun testBackendPact() {

        // Define the pact between frontend and backend. From the perspective of the Frontend team.
        val expectedBody = PactDslJsonArray.arrayEachLike()
            .stringType("name", "example apple")
            .stringType("originCountry", "example country")
            .decimalType("pricePerKilo", 14.3)
            .numberType("amount", 120)

        val pact = ConsumerPactBuilder.consumer("Frontend").hasPactWith("Backend")
            .uponReceiving("a request for all fruits")
                .path("/fruits")
                .method("GET")
            .willRespondWith()
                .status(200)
                .body(expectedBody)
            .toPact()

        // Test the Frontend against a mocked provider (backend), which is definitely compliant with the pact.
        // When the test is passed, the pact file will be created (consumer/build/pacts/Frontend-Backend.json).
        val result: PactVerificationResult = runConsumerTest(pact, MockProviderConfig.createDefault(),
            object : PactTestRun<String> {
                override fun run(mockServer: MockServer, context: PactTestExecutionContext?): String {
                    return runBlocking {
                        val fruits = Frontend(mockServer.getUrl()).getFruits()
                        fruits.shouldBeJsonArray()
                        fruits.shouldContainJsonKey("[0].amount")
                        fruits.shouldContainJsonKey("[0].name")
                        fruits.shouldContainJsonKey("[0].originCountry")
                        fruits.shouldContainJsonKey("[0].pricePerKilo")
                        fruits
                    }
                }
            }
        )

        assert(result is PactVerificationResult.Ok)
    }
}