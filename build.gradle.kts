/*
 * Copyright 2020-2022 NatroxMC team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("build", "shadowJar")

allprojects {
    group = "de.natrox"
    version = "1.0"
    description = "A simple event bus"

    repositories {
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.Natroxmc:common:9b5bbf5fd2")

    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.slf4j:slf4j-api:2.0.0-beta1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-params:5.9.0")
    testImplementation("org.junit.platform:junit-platform-suite-api:1.9.0")
    testRuntimeOnly("org.junit.platform:junit-platform-suite-engine:1.9.0")
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
    options.encoding = "UTF-8"
    options.isIncremental = true

}

publishing {
    publications {
        create<MavenPublication>(project.name) {
            from(components.findByName("java"))
        }
    }
}
