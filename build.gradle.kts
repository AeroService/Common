plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "de.notion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("io.netty:netty-common:4.1.74.Final")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        //Set the Name of the Output File
        archiveFileName.set("${project.name}.jar")
    }
}
