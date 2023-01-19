import io.github.gradlenexus.publishplugin.NexusPublishExtension

/*
 * Copyright 2020-2023 AeroService
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
    id("build-logic")
    alias(libs.plugins.spotless)
    alias(libs.plugins.nexusPublish)
}

defaultTasks("build", "test", "shadowJar")

allprojects {
    group = "org.aero"
    version = "1.0.0-SNAPSHOT"
    description = "A common core library"

    repositories {
        mavenCentral()

        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

subprojects {
    apply(plugin = "maven-publish")

    apply(plugin = "java-library")
    apply(plugin = "checkstyle")
    apply(plugin = "com.diffplug.spotless")

    dependencies {
        "implementation"(rootProject.libs.annotations)
        "implementation"(rootProject.libs.slf4j)

        "testImplementation"(rootProject.libs.bundles.junit)
        "testImplementation"(rootProject.libs.bundles.mockito)
    }

    tasks.withType<Jar> {
        from(rootProject.file("LICENSE"))
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging {
            events("started", "passed", "skipped", "failed")
        }
        systemProperties(System.getProperties().mapKeys { it.key.toString() })
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.encoding = "UTF-8"
        options.isIncremental = true

    }

    tasks.withType<Checkstyle> {
        maxErrors = 0
        maxWarnings = 0
        configFile = rootProject.file("checkstyle.xml")
    }

    extensions.configure<CheckstyleExtension> {
        toolVersion = "10.3.4"
    }

    tasks.register<org.gradle.jvm.tasks.Jar>("javadocJar") {
        archiveClassifier.set("javadoc")
        from(tasks.getByName("javadoc"))
    }

    tasks.register<org.gradle.jvm.tasks.Jar>("sourcesJar") {
        archiveClassifier.set("sources")
        from(project.the<JavaPluginExtension>().sourceSets["main"].allJava)
    }

    configurePublishing("java", true)
}

extensions.configure<NexusPublishExtension> {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))

            username.set(System.getenv("SONATYPE_USER"))
            password.set(System.getenv("SONATYPE_TOKEN"))
        }
    }

    useStaging.set(!rootProject.version.toString().endsWith("-SNAPSHOT"))
}
