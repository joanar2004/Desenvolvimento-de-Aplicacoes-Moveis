plugins {
    // Simple Kotlin/JVM app module that consumes the annotations and processor.
    kotlin("jvm")
    // Runs the annotation processor (from :processor) during compilation.
    kotlin("kapt")
}
group = "org.example"
version = "1.0-SNAPSHOT"
repositories {
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    // Annotations used by the demo code.
    implementation(project(":annotations"))
    // Apply the annotation processor so wrapper classes get generated.
    kapt(project(":processor"))
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    // Toolchain used to compile/run this module.
    jvmToolchain(25)
}
