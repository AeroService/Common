plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("build", "shadowJar")

allprojects {
    group = "de.natrox"
    version = "1.3.0-SNAPSHOT"
    description = "A basic and common library for the development of Java projects"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha7")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
    // options
    options.encoding = "UTF-8"
    options.isIncremental = true
}

tasks.withType<Jar> {
    archiveFileName.set("common.jar")
}
