plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.androidx.paging.common)
    implementation(libs.kotlinx.serialization.json)
    api(libs.kotlinx.collections.immutable)

    testImplementation(libs.junit)
}
