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

import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

tasks {
    withType<KotlinCompile>().configureEach {
        compilerOptions {
            apiVersion = KotlinVersion.DEFAULT
            languageVersion = KotlinVersion.DEFAULT
        }
    }
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        val svgConverterRootPlugin by creating {
            id = libs.plugins.svgConverter.root.get().pluginId
            implementationClass = "design.rakuten.rex.icons.svgconverter.SvgConverterRootPlugin"
        }
        val svgConverterResPlugin by creating {
            id = libs.plugins.svgConverter.res.get().pluginId
            implementationClass = "design.rakuten.rex.icons.svgconverter.SvgConverterResPlugin"
        }
        val svgConverterComposePlugin by creating {
            id = libs.plugins.svgConverter.compose.get().pluginId
            implementationClass = "design.rakuten.rex.icons.svgconverter.SvgConverterComposePlugin"
        }
        val iconGeneratorPlugin by creating {
            id = libs.plugins.iconGenerator.get().pluginId
            implementationClass = "design.rakuten.rex.icons.generator.IconGeneratorPlugin"
        }
        val publishPlugin by creating {
            id = libs.plugins.publish.get().pluginId
            implementationClass = "design.rakuten.rex.icons.publish.PublishPlugin"
        }
    }
}

dependencies {
    compileOnly(libs.android.tools.sdk.common)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    implementation(libs.guava)
    implementation(libs.kotlinPoet)
    implementation(libs.xmlPull)
    implementation(libs.xpp3)
}
