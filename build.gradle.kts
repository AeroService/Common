import com.palantir.gradle.gitversion.VersionDetails

/*
 * Copyright 2020-2022 NatroxMC
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
    id("com.palantir.git-version") version "0.15.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

defaultTasks("build", "shadowJar")

allprojects {
    group = "de.natrox"
    version = "1.0"
    description = "A common core library"

    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("org.slf4j:slf4j-api:2.0.0-alpha7")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
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

if (System.getProperty("publishName") != null && System.getProperty("publishPassword") != null) {
    val versionDetails: groovy.lang.Closure<VersionDetails> by extra
    publishing {
        (components["java"] as AdhocComponentWithVariants).withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
            skip()
        }
        publications {
            create<MavenPublication>(project.name) {
                artifactId = project.name
                version = versionDetails().gitHash
                from(components.findByName("java"))
                pom {
                    name.set(project.name)
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                }
            }
            repositories {
                maven("https://repo.natrox.de/repository/maven-public/") {
                    this.name = "natrox-public"
                    credentials {
                        this.password = System.getProperty("publishPassword")
                        this.username = System.getProperty("publishName")
                    }
                }
            }
        }
    }
}
