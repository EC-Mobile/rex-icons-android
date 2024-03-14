/*
 * Copyright 2024 Rakuten Group, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package design.rakuten.rex.icons.publish

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure

class PublishPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(target) {
        plugins.apply("com.vanniktech.maven.publish.base")
        extensions.configure<PublishingExtension> {
            repositories {
                maven {
                    name = "Temp"
                    setUrl(rootProject.layout.buildDirectory.dir("repo"))
                }
                maven {
                    name = "GitHubPackages"
                    setUrl("https://maven.pkg.github.com/EC-Mobile/rex-icons-android")
                    credentials {
                        username = rootProject.findProperty("github.user") as? String
                            ?: System.getenv("GITHUB_ACTOR")
                        password = rootProject.findProperty("github.token") as? String
                            ?: System.getenv("GITHUB_TOKEN")
                    }
                }
            }
        }
    }
}
