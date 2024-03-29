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

plugins {
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.maven.publish.base) apply false
    alias(libs.plugins.svgConverter.root)
    alias(libs.plugins.svgConverter.res) apply false
    alias(libs.plugins.svgConverter.compose) apply false
    alias(libs.plugins.iconGenerator) apply false
    alias(libs.plugins.publish) apply false
}

task<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}
