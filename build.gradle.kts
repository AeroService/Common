/*
 *
 *  * Copyright 2020-2022 NatroxMC team
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("build", "shadowJar")

allprojects {
    group = "de.natrox"
    version = "1.0.0-SNAPSHOT"
    description = "An simple event bus for Java projects"

    repositories {
        mavenCentral()
    }
}

dependencies {
    //TEMP
    compileOnly(files("D:\\workspace\\NatroxMC\\Common\\build\\libs\\common.jar"))

    implementation(libs.slf4j2)
    implementation(libs.jetbrainsAnnotations)

    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine)
    testImplementation(libs.junit.suite.api)
    testRuntimeOnly(libs.junit.suite.engine)
}

tasks.withType<JavaCompile> {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
    // options
    options.encoding = "UTF-8"
    options.isIncremental = true
}

tasks.withType<Jar> {
    archiveFileName.set("eventbus.jar")
}
