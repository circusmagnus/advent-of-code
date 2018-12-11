import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.10"
}

group = "wojtach.lukasz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile ("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.0.1")
    testCompile("junit", "junit", "4.12")
    testCompile("org.amshove.kluent", "kluent", "1.43")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}