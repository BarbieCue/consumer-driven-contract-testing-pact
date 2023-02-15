plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.3"
}

application {
    mainClass.set("org.example.ApplicationKt")
}

repositories {
    mavenCentral()
}
