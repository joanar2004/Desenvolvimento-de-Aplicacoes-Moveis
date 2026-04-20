plugins {
    // Lightweight JVM Kotlin module that only provides annotation definitions.
    kotlin("jvm")
}
group = "org.example"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    // Toolchain used to compile the annotations module.
    jvmToolchain(25)
}
