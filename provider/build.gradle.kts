val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    kotlin("plugin.serialization").version("1.8.10")
    id("io.ktor.plugin") version "2.2.3"
    id("au.com.dius.pact") version "4.3.10"
}

group = "org.example"
version = "0.0.1"
application {
    mainClass.set("org.example.BackendKt")
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()

    // Always publish verify results to the broker
    systemProperty("pact.verifier.publishResults", "true")
    systemProperty("pact.provider.version", project.version)
    // Pact System Properties: https://docs.pact.io/implementation_guides/jvm/docs/system-properties
}

pact {
    reports {
        defaultReports()
    }
    broker {
        pactBrokerUrl = "http://localhost"
    }
}

dependencies {

    // Server
    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$kotlinVersion")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    // Pact Junit
    testImplementation("au.com.dius.pact.provider:junit:4.4.5")
    testImplementation("au.com.dius.pact.provider:junit5:4.4.5")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}