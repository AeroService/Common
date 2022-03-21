plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
    group = "de.natrox"
    version = "1.0.0-SNAPSHOT"
    description = "A basic and common Library for the development of other projects"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("io.netty:netty-common:4.1.75.Final")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
    // options
    options.encoding = "UTF-8"
    options.isIncremental = true
}

tasks.withType<Jar> {
    archiveFileName.set("Common.jar")
}
