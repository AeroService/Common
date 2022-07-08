plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("build", "shadowJar")

allprojects {
    group = "de.natrox"
    version = "1.0.0-SNAPSHOT"
    description = "A serializer which de/serializes objects of a given type"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation(files("D:\\intelliJ\\natrox\\Common\\build\\libs\\common.jar"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.8.2")
    testRuntimeOnly("org.junit.platform:junit-platform-suite-engine:1.8.2")
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
    // options
    options.encoding = "UTF-8"
    options.isIncremental = true
}
