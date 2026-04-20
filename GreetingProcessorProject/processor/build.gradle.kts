plugins {
    // JVM Kotlin module that implements the annotation processor.
    kotlin("jvm")
    // KAPT is used to run this processor in the consuming module.
    kotlin("kapt")
}
group = "org.example"
version = "1.0-SNAPSHOT"
repositories {
    // KotlinPoet and AutoService are resolved from Maven Central.
    mavenCentral()
}
dependencies {
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    // Registers this processor via META-INF/services for discovery by annotation processing tools.
    implementation("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
    // Code generation library used to produce the wrapper Kotlin sources.
    implementation("com.squareup:kotlinpoet:1.14.2")
    // Compile against the annotation types.
    implementation(project(":annotations"))
}
kapt {
    // Helps kapt handle types it cannot resolve precisely in some configurations.
    correctErrorTypes = true
}
tasks.test {
    useJUnitPlatform()
}
kotlin {
    // Target JDK toolchain used to compile this module.
    jvmToolchain(25)
}
