val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
    id("au.com.dius.pact") version "4.3.10"
}

group = "org.example"
version = "0.0.1"
application {
    mainClass.set("org.example.FrontendKt")
}

repositories {
    mavenCentral()
}

tasks.test {
    useJUnitPlatform()
}

pact {
    broker {
        pactBrokerUrl = "http://localhost"
    }
    publish {
        pactDirectory = "$rootDir/build/pacts"
    }
}

dependencies {

    // Client
    implementation("io.ktor:ktor-client-core:$kotlinVersion")
    implementation("io.ktor:ktor-client-cio:$kotlinVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$kotlinVersion")
    implementation("io.ktor:ktor-client-json:$kotlinVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$kotlinVersion")

    // Testing
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("io.kotest:kotest-assertions-json-jvm:5.5.5")

    // Pact Junit
    testImplementation("au.com.dius.pact.consumer:junit5:4.4.5")
    testImplementation("au.com.dius.pact:consumer:4.4.5")

    // Logging
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
}