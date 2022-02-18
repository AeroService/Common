plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "de.notion"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("io.netty:netty-common:4.1.58.Final")
}

/*
if (System.getProperty("publishName") != null && System.getProperty("publishPassword") != null) {
    publishing {
        (components["java"] as AdhocComponentWithVariants).withVariantsFromConfiguration(configurations["shadowRuntimeElements"]) {
            skip()
        }
        publications {
            create<MavenPublication>(project.name) {
                groupId = "de.natrox"
                artifactId = "natrox-common"
                version = "1.0-SNAPSHOT"
                from(components.findByName("java"))
                pom {
                    name.set(project.name)
                    properties.put("inceptionYear", "2021")
                    developers {
                        developer {
                            id.set("dasdrolpi")
                            name.set("Lars")
                            email.set("admin@natrox.de")
                        }
                    }
                }
            }
            repositories {
                maven("https://repo.natrox.de/repository/maven-internal/") {
                    this.name = "natrox-internal"
                    credentials {
                        this.password = System.getProperty("publishPassword")
                        this.username = System.getProperty("publishName")
                    }
                }
            }
        }
    }
}

 */

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    shadowJar {
        //Set the Name of the Output File
        archiveFileName.set("${project.name}.jar")
    }
}
