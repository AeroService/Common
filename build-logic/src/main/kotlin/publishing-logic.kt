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

import org.gradle.api.Project
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure

fun Project.configurePublishing(publishedComponent: String, withJavadocAndSource: Boolean = false) {
    extensions.configure<PublishingExtension> {
        publications.apply {
            create("maven", MavenPublication::class.java).apply {
                from(components.getByName(publishedComponent))

                if (withJavadocAndSource) {
                    artifact(tasks.getByName("sourcesJar"))
                    artifact(tasks.getByName("javadocJar"))
                }

                pom.apply {
                    name.set(project.name)
                    description.set(project.description)

                    developers {
                        developer {
                            id.set("drolpi")
                            email.set("drolpiofficial@gmail.com")
                            timezone.set("Europe/Berlin")
                        }
                    }

                    licenses {
                        license {
                            name.set("Apache License, Version 2.0")
                            url.set("https://opensource.org/licenses/Apache-2.0")
                        }
                    }

                    scm {
                        tag.set("HEAD")
                        url.set("git@github.com:AeroService/Common.git")
                        connection.set("scm:git:git@github.com:AeroService/Common.git")
                        developerConnection.set("scm:git:git@github.com:AeroService/Common.git")
                    }

                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("https://github.com/AeroService/Common/issues")
                    }

                    withXml {
                        val repositories = asNode().appendNode("repositories")
                        project.repositories.forEach {
                            if (it is MavenArtifactRepository && it.url.toString().startsWith("https://")) {
                                val repo = repositories.appendNode("repository")
                                repo.appendNode("id", it.name)
                                repo.appendNode("url", it.url.toString())
                            }
                        }
                    }
                }
            }
        }
    }
}
